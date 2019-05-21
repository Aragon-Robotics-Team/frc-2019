package frc.robot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class BetterTalonSRX implements BetterSendable, BetterSpeedController {
    final Deadband deadband;

    public final TalonSRX talon;
    final double ticksPerInch;
    int timeout = 300; // milliseconds
    final SendableSRX sendable;
    double lastOutput;
    final boolean isReal;
    SensorCollection sensorCollection;
    List<BetterFollower> slaves;
    final double maxTickVelocity;
    final double zeroPosition;
    final boolean debug;

    enum ControlType {
        Percent, Magic, OldPercent;
    }

    ControlType lastControlType = ControlType.Percent;

    public BetterTalonSRX(Integer canID, BetterTalonSRXConfig config) {
        isReal = canID != null;
        if (!isReal) {
            timeout = 0; // Speed up simulation?
        }
        talon = Mock.createMockable(TalonSRX.class, canID);

        config.prepare();

        talon.configAllSettings(config, timeout);
        talon.setSensorPhase(config.invertEncoder);
        talon.setInverted(config.invert);
        talon.setNeutralMode(config.neutralMode);

        talon.selectProfileSlot(0, 0);

        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, timeout);
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, timeout);

        if (config.voltageCompSaturation != 0.0) {
            talon.enableVoltageCompensation(true);
        }

        if (config.peakCurrentLimit != 0.0) {
            talon.enableCurrentLimit(true);
        }

        sendable = new SendableSRX(this);
        // If two different sensors are configured, we have a second sensor
        sendable.secondSensor =
                config.primaryPID.selectedFeedbackSensor != config.auxiliaryPID.selectedFeedbackSensor;
        deadband = config.deadband;

        ticksPerInch = config.ticksPerInch;
        slaves = new ArrayList<BetterFollower>(1); // 1 max expected follower
        maxTickVelocity = config.maxTickVelocity;
        zeroPosition = config.zeroPosition;
        debug = config.debug;

        resetEncoder();

        sensorCollection = isReal ? talon.getSensorCollection() : Mock.mock(SensorCollection.class);

        if (config.encoder == BetterTalonSRXConfig.Encoder.CTREMag && config.crossZeroMag != null) {
            int low = config.lowTickMag;
            int high = config.highTickMag;
            boolean zero = config.crossZeroMag;

            sensorCollection.syncQuadratureWithPulseWidth(low, high, zero, 0, timeout);
            setMagic(zeroPosition);
            if (debug) {
                System.out.printf("%s mag encoder %s %s\n", talon.getBaseID(), low, high, zero);
            }
        } else if (debug) {
            System.out.println(talon.getBaseID() + " not syncing mag encoder");
        }

        if (debug) {
            System.out.println(
                    canID + " kF: " + config.slot0.kF + " maxV: " + config.maxTickVelocity);
        }
    }

    public BetterTalonSRX(int deviceNumber) {
        this(deviceNumber, new BetterTalonSRXConfig());
    }

    public void createSendable(SendableMaster master) {
        // Usually this method is called after init zero position/speed is set in subsystem
        // So if they set a magic type, then we are probably going to use magic
        sendable.isMagic = lastControlType == ControlType.Magic;
        master.add(sendable);

        timeout = 0; // Done configuring by now, so can disable timeout
    }

    // Setting Output

    public void set(double output) {
        setOldPercent(output);
    }

    public void setOld(double output) {
        if (lastControlType == ControlType.Percent) {
            setPercent(output);
        } else if (lastControlType == ControlType.Magic) {
            setMagic(output);
        } else if (lastControlType == ControlType.OldPercent) {
            setOldPercent(output);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void setPercent(double percent) {
        double max = 1;
        lastControlType = ControlType.Percent;
        lastOutput = deadband.calc(percent) * max; // * maxTickVelocity;

        // Velocity PIDF: Need kF and kP minimum
        talon.set(ControlMode.Velocity, lastOutput);
    }

    public void setOldPercent(double output) {
        lastControlType = ControlType.OldPercent;
        lastOutput = deadband.calc(output);

        talon.set(ControlMode.PercentOutput, lastOutput);
    }

    public void setMagic(double output) {
        lastControlType = ControlType.Magic;
        lastOutput = output;

        talon.set(ControlMode.MotionMagic, lastOutput * ticksPerInch);
    }

    // Getting Output

    public double get() {
        return isReal ? talon.getMotorOutputPercent() : lastOutput;
    }

    public double getDesired() {
        return this.lastOutput;
    }

    // Encoder

    public double getEncoderRate() {
        return talon.getSelectedSensorVelocity(0);
    }

    public double getEncoderPos() {
        return talon.getSelectedSensorPosition(0);
    }

    public void resetEncoder() {
        talon.setSelectedSensorPosition((int) zeroPosition, 0, timeout);
        setMagic(zeroPosition);
    }

    public double getInch() {
        return getEncoderPos() / ticksPerInch;
    }

    // Motion Magic

    public double getSet() {
        return talon.getClosedLoopTarget() / ticksPerInch;
    }

    // Limit Switch

    public boolean getForwardLimitSwitch() {
        return sensorCollection.isFwdLimitSwitchClosed();
    }

    public boolean getReverseLimitSwitch() {
        return sensorCollection.isRevLimitSwitchClosed();
    }

    // Other setters

    public void setBrakeMode(boolean brake) {
        NeutralMode neutralMode = brake ? NeutralMode.Brake : NeutralMode.Coast;

        talon.setNeutralMode(neutralMode);

        for (BetterFollower slave : slaves) {
            slave.setBrakeMode(neutralMode);
        }
    }

    public void addFollower(BetterFollower slave) { // Add all followers before setting brake mode
        slaves.add(slave);
        slave.follow(talon);
    }

    public void setOpenLoopRamp(double ramp) {
        talon.configOpenloopRamp(ramp, timeout);
    }
}


class SendableSRX extends SendableBase {
    BetterTalonSRX t;
    boolean isMagic;
    boolean secondSensor;

    public SendableSRX(BetterTalonSRX talon) {
        this.t = talon;
    }

    // Magic Wrapper; only call supplier if in magic
    private DoubleSupplier magic(DoubleSupplier supplier) {
        return new DoubleSupplier() {
            public double getAsDouble() {
                if (t.lastControlType == BetterTalonSRX.ControlType.Magic) {
                    return supplier.getAsDouble();
                } else {
                    return -1;
                }
            }
        };
    }

    public void initSendable(SendableBuilder b) {
        if (t.debug) {
            System.out.printf("%s Magic %s 2Sensor %s", t.talon.getBaseID(), isMagic, secondSensor);
        }

        b.addDoubleProperty("Output", t::get, t::set);
        b.addDoubleProperty("Velocity", t::getEncoderRate, null);
        b.addDoubleProperty("Distance", t::getEncoderPos, null);
        if (secondSensor) {
            b.addDoubleProperty("Distance 2", () -> t.talon.getSelectedSensorPosition(1), null);
        }

        if (isMagic) {
            b.addDoubleProperty("MagicError", magic(t.talon::getClosedLoopError), null);
            b.addDoubleProperty("MagicTarget", magic(t.talon::getClosedLoopTarget), null);
            b.addDoubleProperty("MagicVel", magic(t.talon::getActiveTrajectoryVelocity), null);
            b.addDoubleProperty("MagicPos", magic(t.talon::getActiveTrajectoryPosition), null);

            b.addDoubleProperty("Wanted Inches", magic(t::getSet), t::set);
            b.addDoubleProperty("Current Inches", magic(t::getInch), null);

            // b.addDoubleProperty("Integral Accumator", magic(t.talon::getIntegralAccumulator),
            // null);
        }

        b.addBooleanProperty("Reverse Limit", t::getReverseLimitSwitch, null);
        b.addDoubleProperty("Desired Output", t::getDesired, null);
        // b.addBooleanProperty("Forward Limit", t::getForwardLimitSwitch, null);
        b.addDoubleProperty("Current", t.talon::getOutputCurrent, null);
    }
}
