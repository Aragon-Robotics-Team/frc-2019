package frc.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj.Encoder;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.Faults;
// import edu.wpi.first.wpilibj.
import frc.robot.RobotMap;

public class Drivetrain extends Subsystem {
    static double speedModifer = -1.0;

    TalonSRX LeftWheels;
    TalonSRX RightWheels;
    Faults leftFaults;
    Faults rightFaults;
    DifferentialDrive differentialDrive;
    Encoder leftEncoder;
    Encoder rightEncoder;

    double lDistance;
    double rDistance;

    public Drivetrain() {
        LeftWheels = new TalonSRX(RobotMap.LeftWheelsCan);
        RightWheels = new TalonSRX(RobotMap.RightWheelsCan);
        LeftWheels.setInverted(false);
        RightWheels.setInverted(true);
        LeftWheels.setSensorPhase(false);
        RightWheels.setSensorPhase(false);

        leftFaults = new Faults();
        rightFaults = new Faults();

        leftEncoder = new Encoder(1, 2, false, Encoder.EncodingType.k4X);
        leftEncoder.setDistancePerPulse(3.0 / 1024.0);
        // leftEncoder.setSamplesToAverage(127);

        rightEncoder = new Encoder(3, 4, true, Encoder.EncodingType.k4X);
        rightEncoder.setDistancePerPulse(3.0 / 1024.0);
        // rightEncoder.setSamplesToAverage(127);

        lDistance = 0.0;
        rDistance = 0.0;
    }

    public void initDefaultCommand() {
    }

    public void control(double x, double y) {
        LeftWheels.set(ControlMode.PercentOutput, x);
        RightWheels.set(ControlMode.PercentOutput, y);
        // System.out.println("Control: " + x + " " + y);
    }

    public void refreshDashboard() {
        refreshFaults();

        SmartDashboard.putNumber("Sensor Vel", RightWheels.getSelectedSensorVelocity());
        SmartDashboard.putNumber("Sensor Pos", RightWheels.getSelectedSensorPosition());
        SmartDashboard.putNumber("Out %", RightWheels.getMotorOutputPercent());
        SmartDashboard.putBoolean("Out Of Phase", rightFaults.SensorOutOfPhase);
    }

    public void controlArcade(double x, double y) {
        // Implementation stolen from DifferentialDrive.class WPILib

        double maxInput = Math.copySign(Math.max(Math.abs(x), Math.abs(y)), x);

        if (x * y >= 0.0) { // If both sign are the same
            control(maxInput, x - y);
        } else {
            control(x + y, maxInput);
        }
    }

    public void goForward(double x) {
        control(x, x);
        // System.out.println("Control: " + x);
    }

    public void stop() {
        control(0, 0);
        // System.out.println("stop");
    }

    public double getLeftRate() {
        return leftEncoder.getRate();
    }

    public double getRightRate() {
        return rightEncoder.getRate();
    }

    public double getLeftDistance() {
        return leftEncoder.getDistance() + lDistance;
    }

    public double getRightDistance() {
        return rightEncoder.getDistance() + rDistance;
    }

    public double getRawLeftDistance() {
        return leftEncoder.getDistance();
    }

    public double getRawRightDistance() {
        return rightEncoder.getDistance();
    }

    public void resetDistance() {
        lDistance = getLeftDistance();
        rDistance = getRightDistance();

        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void refreshFaults() {
        LeftWheels.getFaults(leftFaults);
        RightWheels.getFaults(rightFaults);
    }
}
