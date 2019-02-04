package frc.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.Encoder;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
// import edu.wpi.first.wpilibj.
import frc.robot.RobotMap;

public class Drivetrain extends Subsystem {
    static double speedModifer = -1.0;

    TalonSRX LeftWheels;
    TalonSRX RightWheels;
    DifferentialDrive differentialDrive;
    Encoder leftEncoder;
    Encoder rightEncoder;

    double lDistance;
    double rDistance;

    double qsAccumulator;

    public Drivetrain() {
        LeftWheels = new TalonSRX(RobotMap.LeftWheelsCan);
        RightWheels = new TalonSRX(RobotMap.RightWheelsCan);
        LeftWheels.setInverted(false);
        RightWheels.setInverted(true);

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

    public void controlArcade(double x, double y) {
        // Implementation stolen from DifferentialDrive.class WPILib

        double maxInput = Math.copySign(Math.max(Math.abs(x), Math.abs(y)), x);

        if (x * y >= 0.0) { // If both sign are the same
            control(maxInput, x - y);
        } else {
            control(x + y, maxInput);
        }
    }

    public void controlCheezy(double x, double y, boolean quickTurn) {
        // Implementation stolen from DifferentialDrive.class WPILib

        final double qsThreshold = 0.2;
        final double qsAlpha = 0.1;

        double turn;
        boolean overPower;

        if (quickTurn) {
            if (Math.abs(x) < qsThreshold) {
                qsAccumulator = (1 - qsAlpha) * qsAccumulator + qsAlpha * y * 2;
            }
            overPower = true;
            turn = y;
        } else {
            overPower = false;
            turn = Math.abs(x) * y - qsAccumulator;

            if (qsAccumulator > 1) {
                qsAccumulator -= 1;
            } else if (qsAccumulator < -1) {
                qsAccumulator += 1;
            } else {
                qsAccumulator = 0.0;
            }
        }

        double left = x + turn;
        double right = x - turn;

        // If rotation is overpowered, reduce both outputs to within acceptable range
        if (overPower) {
            if (left > 1.0) {
                right -= left - 1.0;
                left = 1.0;
            } else if (right > 1.0) {
                left -= right - 1.0;
                right = 1.0;
            } else if (left < -1.0) {
                right -= left + 1.0;
                left = -1.0;
            } else if (right < -1.0) {
                left -= right + 1.0;
                right = -1.0;
            }
        }

        // Normalize the wheel speeds
        double maxMagnitude = Math.max(Math.abs(left), Math.abs(right));
        if (maxMagnitude > 1.0) {
            left /= maxMagnitude;
            right /= maxMagnitude;
        }

        control(left, right);
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
}
