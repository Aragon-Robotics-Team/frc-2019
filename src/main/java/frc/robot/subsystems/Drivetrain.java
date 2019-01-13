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

        SmartDashboard.putNumber("Right Sensor Vel", RightWheels.getSelectedSensorVelocity());
        SmartDashboard.putNumber("Right Sensor Pos", RightWheels.getSelectedSensorPosition());
        SmartDashboard.putNumber("Right Out %", RightWheels.getMotorOutputPercent());
        SmartDashboard.putBoolean("Right Out Of Phase", rightFaults.SensorOutOfPhase);

        SmartDashboard.putNumber("Left Sensor Vel", LeftWheels.getSelectedSensorVelocity());
        SmartDashboard.putNumber("Left Sensor Pos", LeftWheels.getSelectedSensorPosition());
        SmartDashboard.putNumber("Left Out %", LeftWheels.getMotorOutputPercent());
        SmartDashboard.putBoolean("Left Out Of Phase", leftFaults.SensorOutOfPhase);
    }

    public void controlArcade(double x, double y) {
        // differentialDrive.arcadeDrive(x*speedModifer, y*speedModifer, false);
        // differentialDrive.arca
        // System.out.println("Control: " + x + " " + y);
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
