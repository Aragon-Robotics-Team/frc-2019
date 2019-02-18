package frc.robot.util;

import static org.mockito.Mockito.mock;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;

public class BetterTalonSRX {
    Deadband deadband;

    TalonSRX talon;
    double ticksPerInch;
    int timeout = 300; // milliseconds
    SendableSRX sendable;
    double lastOutput;
    boolean isReal;
    SensorCollection sensorCollection;

    enum ControlType {
        Percent, Magic;
    }

    ControlType lastControlType = ControlType.Percent;

    public BetterTalonSRX(int deviceNumber, BetterTalonSRXConfig config) {
        talon = config.isConnected ? (new TalonSRX(deviceNumber)) : mock(TalonSRX.class);
        talon.configFactoryDefault(timeout);

        talon.configAllSettings(config, timeout);
        talon.setSensorPhase(config.invertEncoder);
        talon.setInverted(config.invert);
        talon.setNeutralMode(config.neutralMode);

        talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, timeout);
        talon.selectProfileSlot(0, 0);

        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, timeout);
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, timeout);

        resetEncoder();

        sendable = new SendableSRX(this);
        deadband = config.deadband;
        sensorCollection =
                config.isConnected ? talon.getSensorCollection() : mock(SensorCollection.class);

        timeout = 0;
        isReal = config.isConnected;
        ticksPerInch = config.ticksPerInch;
    }

    public BetterTalonSRX(int deviceNumber) {
        this(deviceNumber, new BetterTalonSRXConfig());
    }

    public void addShuffleboard(ShuffleboardTab tab, String name) {
        tab.add(name, sendable);
    }

    // Setting Output

    public void set(double output) {
        if (lastControlType == ControlType.Percent) {
            setPercent(output);
        } else if (lastControlType == ControlType.Magic) {
            setMagic(output);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void setPercent(double output) {
        lastControlType = ControlType.Percent;
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
        talon.setSelectedSensorPosition(0, 0, timeout);
        setMagic(0.0);
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

    public void setBrakeMode(boolean brake) {
        if (brake) {
            talon.setNeutralMode(NeutralMode.Brake);
        } else {
            talon.setNeutralMode(NeutralMode.Coast);
        }
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

        builder.addDoubleProperty("MagicError", talon.talon::getClosedLoopError, null);
        builder.addDoubleProperty("MagicTarget", talon.talon::getClosedLoopTarget, null);
        builder.addDoubleProperty("MagicVelocity", talon.talon::getActiveTrajectoryVelocity, null);
        builder.addDoubleProperty("MagicPosition", talon.talon::getActiveTrajectoryPosition, null);

        builder.addDoubleProperty("Wanted Inches", talon::getSet, talon::set);
        builder.addDoubleProperty("Current Inches", talon::getInch, null);

        builder.addBooleanProperty("Reverse Limit", talon::getReverseLimitSwitch, null);
    }
}
