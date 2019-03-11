package frc.robot.util;

import static frc.robot.util.Mock.mock;
import java.util.ArrayList;
import java.util.List;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class BetterTalonSRX implements BetterSendable {
    Deadband deadband;

    TalonSRX talon;
    double ticksPerInch;
    int timeout = 300; // milliseconds
    SendableSRX sendable;
    double lastOutput;
    boolean isReal;
    SensorCollection sensorCollection;
    List<BetterFollower> slaves;
    double maxTickVelocity;
    double zeroPosition;

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

        sendable = new SendableSRX(this);
        deadband = config.deadband;

        timeout = 0;
        ticksPerInch = config.ticksPerInch;
        slaves = new ArrayList<BetterFollower>(1); // 1 max expected follower
        maxTickVelocity = config.maxTickVelocity;
        zeroPosition = config.zeroPosition;

        resetEncoder();

        sensorCollection = isReal ? talon.getSensorCollection() : mock(SensorCollection.class);

        if (config.encoder == BetterTalonSRXConfig.Encoder.CTREMag && config.crossZeroMag != null) {
            int low = config.lowTickMag;
            int high = config.highTickMag;
            boolean zero = config.crossZeroMag;
            System.out.println(canID + " syncing mag encoder: " + low + " " + high + " " + zero);

            sensorCollection.syncQuadratureWithPulseWidth(low, high, zero, 0, timeout);
            setMagic(zeroPosition);
        } else {
            System.out.println(canID + " not syncing mag encoder");
        }

        System.out.println(canID + " kF: " + config.slot0.kF + " maxV: " + config.maxTickVelocity);
    }

    public BetterTalonSRX(int deviceNumber) {
        this(deviceNumber, new BetterTalonSRXConfig());
    }

    public void createSendable(SendableMaster master) {
        master.add(sendable);
    }

    // Setting Output

    public void set(double output) {
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
}


class SendableSRX extends SendableBase {
    BetterTalonSRX talon;

    public SendableSRX(BetterTalonSRX talon) {
        this.talon = talon;
    }

    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Output", talon::get, talon::set);
        builder.addDoubleProperty("Velocity", talon::getEncoderRate, null);
        builder.addDoubleProperty("Distance", talon::getEncoderPos, null);
        builder.addDoubleProperty("Distance 2", () -> talon.talon.getSelectedSensorPosition(1),
                null);

        builder.addDoubleProperty("MagicError", talon.talon::getClosedLoopError, null);
        builder.addDoubleProperty("MagicTarget", talon.talon::getClosedLoopTarget, null);
        builder.addDoubleProperty("MagicVelocity", talon.talon::getActiveTrajectoryVelocity, null);
        builder.addDoubleProperty("MagicPosition", talon.talon::getActiveTrajectoryPosition, null);

        builder.addDoubleProperty("Wanted Inches", talon::getSet, talon::set);
        builder.addDoubleProperty("Current Inches", talon::getInch, null);

        builder.addBooleanProperty("Reverse Limit", talon::getReverseLimitSwitch, null);
    }
}
