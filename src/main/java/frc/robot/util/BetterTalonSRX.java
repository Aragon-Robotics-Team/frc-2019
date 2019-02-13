package frc.robot.util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.util.Deadband;

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

        setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, timeout);
        setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, timeout);

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
        if (lastControlType == ControlType.Percent) {
            return getMotorOutputPercent();
        } else if (lastControlType == ControlType.Magic) {
            return getClosedLoopTarget();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void disable() {
        set(0);
    }

    public void stopMotor() {
        set(0);
    }

    public double getEncoderRate() {
        return getSelectedSensorVelocity();
    }

    public double getEncoderPos() {
        return getSelectedSensorPosition();
    }

    public double getRate() {
        return getMotorOutputPercent();
    }

    public ErrorCode configOpenloopRamp(double seconds) {
        return configOpenloopRamp(2, timeout); // seconds
    }

    public void resetEncoder() {
        setSelectedSensorPosition(0, 0, 0);
    }

    public void setPID(PIDGains pid) {
        config_kP(0, pid.kP, timeout);
        config_kI(0, pid.kI, timeout);
        config_kD(0, pid.kD, timeout);
        config_kF(0, pid.kF, timeout);

        configMotionCruiseVelocity(pid.kV, timeout);
        configMotionAcceleration(pid.kA, timeout);

    }

    public void pidWrite(double output) {
        set(output);
    }
}


class SendableEncoderSRX extends SendableBase {
    BetterTalonSRX talon;

    public SendableEncoderSRX(BetterTalonSRX talon) {
        this.talon = talon;
    }

    public double getDistancePerTick() {
        return 1680;
    }

    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Quadrature Encoder");
        builder.addDoubleProperty("Speed", talon::getEncoderRate, null);
        builder.addDoubleProperty("Distance", talon::getEncoderPos, null);
        builder.addDoubleProperty("Distance per Tick", this::getDistancePerTick, null);

        builder.addDoubleProperty("ClosedLoopError", talon::getClosedLoopError, null);
        builder.addDoubleProperty("ClosedLoopTarget", talon::getClosedLoopTarget, null);
        builder.addDoubleProperty("ActTrajVelocity", talon::getActiveTrajectoryVelocity, null);
        builder.addDoubleProperty("ActTrajPosition", talon::getActiveTrajectoryPosition, null);
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
