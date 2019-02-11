package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj.Encoder;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import java.util.HashMap;
import java.util.Map;
import com.ctre.phoenix.motorcontrol.ControlMode;
// import edu.wpi.first.wpilibj.
import frc.robot.RobotMap;
import frc.robot.util.BetterTalonSRX;

public class Drivetrain extends Subsystem {
    static double speedModifer = -1.0;

    BetterTalonSRX LeftWheels;
    BetterTalonSRX RightWheels;
    DifferentialDrive differentialDrive;
    Encoder leftEncoder;
    Encoder rightEncoder;

    double lDistance;
    double rDistance;

    public NetworkTableEntry rampSet;

    public Drivetrain() {
        LeftWheels = new BetterTalonSRX(RobotMap.LeftWheelsCan, false);
        RightWheels = new BetterTalonSRX(RobotMap.RightWheelsCan, true);
        ShuffleboardTab tab = Shuffleboard.getTab("Drive");
        LeftWheels.addShuffleboard(tab, "Left Wheels");
        RightWheels.addShuffleboard(tab, "Right Wheels");

        leftEncoder = new Encoder(1, 2, false, Encoder.EncodingType.k4X);
        leftEncoder.setDistancePerPulse(3.0 / 1024.0);
        // leftEncoder.setSamplesToAverage(127);

        rightEncoder = new Encoder(3, 4, true, Encoder.EncodingType.k4X);
        rightEncoder.setDistancePerPulse(3.0 / 1024.0);
        // rightEncoder.setSamplesToAverage(127);

        lDistance = 0.0;
        rDistance = 0.0;

        rampSet =
                Shuffleboard.getTab("Drive").add("Ramp", 0).withWidget(BuiltInWidgets.kNumberSlider)
                        .withProperties(Map.of("Min", 0, "Max", 5)).getEntry();
    }

    public void initDefaultCommand() {
    }

    public void control(double x, double y) {
        LeftWheels.set(x);
        RightWheels.set(y);
        // System.out.println("Control: " + x + " " + y);
    }

    public void controlArcade(double x, double y) { // x is up/down; y is right/left
        // differentialDrive.arcadeDrive(x, y, false);

        // double rp = 0;
        // double lp = 0;
        // rp += x;
        // lp += x;
        // rp -= y;
        // lp += y;
        // control(lp, rp);

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

    public void setOpenloopRamp(double ramp) {
        LeftWheels.configOpenloopRamp(ramp);
        RightWheels.configOpenloopRamp(ramp);
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
