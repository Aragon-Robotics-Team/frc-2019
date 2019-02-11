package frc.robot.util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.util.Deadband;

public class BetterTalonSRX extends TalonSRX implements SpeedController {
    double speed;
    static Deadband deadband = new Deadband(0.125, 0); // Warning: 0 deadband!;
    public static final int timeout = 30; // milliseconds
    SendableEncoderSRX encoderSendable;
    SendableMotorSRX motorSendable;

    public BetterTalonSRX(int deviceNumber, boolean invert) {
        super(deviceNumber);
        TalonSRXConfiguration config = new TalonSRXConfiguration();
        config.openloopRamp = 0.5;
        configAllSettings(config, timeout);
        setInverted(invert);
        setNeutralMode(NeutralMode.Brake);

        encoderSendable = new SendableEncoderSRX(this);
        motorSendable = new SendableMotorSRX(this);
    }

    public BetterTalonSRX(int deviceNumber) {
        this(deviceNumber, false);
    }

    public void addShuffleboard(ShuffleboardTab tab) {
        int id = this.getDeviceID();
        System.out.println("InitTab" + id);
        tab.add("BetterSRX " + id + " Encoder", encoderSendable);
        tab.add("BetterSRX " + id + " Motor", motorSendable);
    }

    public void set(double output) {
        speed = output;

        output = deadband.calc(output);

        set(ControlMode.PercentOutput, output);
    }

    public double get() {
        return speed;
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
        builder.addDoubleProperty("Value", talon::getRate, talon::set);
    }
}
