package frc.robot.util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class BetterTalonSRX extends TalonSRX implements SpeedController {
    static Deadband deadband = new Deadband(0.125, 0); // Warning: 0 deadband!;
    public static final int timeout = 300; // milliseconds
    SendableEncoderSRX encoderSendable;
    SendableMotorSRX motorSendable;

    private enum ControlType {
        Percent, Magic;
    }

    private ControlType lastControlType = ControlType.Percent;

    public BetterTalonSRX(int deviceNumber, boolean invert, boolean encoderInvert) {
        super(deviceNumber);
        configFactoryDefault(timeout);

        TalonSRXConfiguration config = new TalonSRXConfiguration();
        config.openloopRamp = 0.1;
        configAllSettings(config, timeout);

        setSensorPhase(encoderInvert);
        setInverted(invert);
        setNeutralMode(NeutralMode.Brake);
        configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, timeout);
        selectProfileSlot(0, 0);

        setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, timeout);
        setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, timeout);

        configNominalOutputForward(0.1, timeout);
        configNominalOutputReverse(0.1, timeout);
        configPeakOutputForward(1, timeout);
        configPeakOutputReverse(-1, timeout);

        resetEncoder();

        encoderSendable = new SendableEncoderSRX(this);
        motorSendable = new SendableMotorSRX(this);
    }

    public BetterTalonSRX(int deviceNumber) {
        this(deviceNumber, false, false);
    }

    public BetterTalonSRX(int deviceNumber, boolean invert) {
        this(deviceNumber, invert, false);
    }

    public void addShuffleboard(ShuffleboardTab tab, String name) {
        tab.add(name + " Encoder", encoderSendable);
        tab.add(name + " Motor", motorSendable);
    }

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

        output = deadband.calc(output);

        set(ControlMode.PercentOutput, output);
    }

    public void setMagic(double output) {
        lastControlType = ControlType.Magic;

        set(ControlMode.MotionMagic, output);
    }

    public double get() {
        return getMotorOutputPercent();
    }

    public void disable() {
        set(0);
    }

    public void stopMotor() {
        set(0);
    }

    public double getEncoderRate() {
        return getSelectedSensorVelocity(0);
    }

    public double getEncoderPos() {
        return getSelectedSensorPosition(0);
    }

    public double getRate() {
        return getMotorOutputPercent();
    }

    public ErrorCode configOpenloopRamp(double seconds) {
        return configOpenloopRamp(seconds, timeout); // seconds
    }

    public void resetEncoder() {
        setSelectedSensorPosition(0, 0, timeout);
    }

    public void setPID(PIDGains pid) {
        config_kP(0, pid.kP, timeout);
        config_kI(0, pid.kI, timeout);
        config_kD(0, pid.kD, timeout);
        config_kF(0, pid.kF, timeout);

        configMotionCruiseVelocity(pid.kV, timeout);
        configMotionAcceleration(pid.kA, timeout);

        configAllowableClosedloopError(0, pid.maxError, timeout);
    }

    public void pidWrite(double output) {
        set(output);
    }
}

class SendableEncoderSRX extends SendableBase {
    BetterTalonSRX talon;
    double set;

    public SendableEncoderSRX(BetterTalonSRX talon) {
        this.talon = talon;
    }

    public double getDistancePerTick() {
        return 1680;
    }

    public double getClosedLoopError() {
        return talon.getClosedLoopError(0);
    }

    public double getClosedLoopTarget() {
        return talon.getClosedLoopTarget(0);
    }

    public double getActiveTrajectoryVelocity() {
        return talon.getActiveTrajectoryVelocity(0);
    }

    public double getActiveTrajectoryPosition() {
        return talon.getActiveTrajectoryPosition(0);
    }

    public double getSet() {
        return set;
    }

    public void setSet(double set) {
        this.set = set;
        talon.setMagic(set * 254.625);
    }

    public double getInch() {
        return talon.getEncoderPos() / 254.625;
    }

    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Speed", talon::getEncoderRate, null);
        builder.addDoubleProperty("Distance", talon::getEncoderPos, null);
        builder.addDoubleProperty("Distance per Tick", this::getDistancePerTick, null);

        builder.addDoubleProperty("ClosedLoopError", this::getClosedLoopError, null);
        builder.addDoubleProperty("ClosedLoopTarget", this::getClosedLoopTarget, null);
        builder.addDoubleProperty("ActTrajVelocity", this::getActiveTrajectoryVelocity, null);
        builder.addDoubleProperty("ActTrajPosition", this::getActiveTrajectoryPosition, null);

        builder.addDoubleProperty("Wanted Inches", this::getSet, this::setSet);
        builder.addDoubleProperty("Current Inches", this::getInch, null);
    }
}

class SendableMotorSRX extends SendableBase {
    BetterTalonSRX talon;

    public SendableMotorSRX(BetterTalonSRX talon) {
        this.talon = talon;
    }

    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Speed Controller");
        builder.setActuator(true);
        builder.setSafeState(() -> {
        });
        builder.addDoubleProperty("Value", talon::get, talon::set);
    }
}
