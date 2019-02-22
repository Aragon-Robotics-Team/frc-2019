package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.RobotMap;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;

public class Drivetrain extends Subsystem {
    BetterTalonSRX leftController;
    BetterTalonSRX rightController;

    ShuffleboardTab tab;
    DrivetrainSendable drivetrainSendable;

    public Drivetrain() {
        BetterTalonSRXConfig leftConfig = new BetterTalonSRXConfig();
        leftConfig.isConnected = RobotMap.DRIVETRAIN_LEFT_MAIN_INSTALLED;
        leftConfig.invert = false;
        // (tick_speed for 100% output) / (max measured tick_speed)
        leftConfig.slot0.kF = 1023.0 / 1150;
        leftController = new BetterTalonSRX(RobotMap.DRIVETRAIN_LEFT_MAIN_CAN, leftConfig);

        BetterTalonSRXConfig rightConfig = new BetterTalonSRXConfig();
        rightConfig.isConnected = RobotMap.DRIVETRAIN_RIGHT_MAIN_INSTALLED;
        rightConfig.invert = true;
        leftConfig.slot0.kF = 1023.0 / 1150;
        rightController = new BetterTalonSRX(RobotMap.DRIVETRAIN_RIGHT_MAIN_CAN, rightConfig);

        tab = Shuffleboard.getTab("Drive");
        leftController.addShuffleboard(tab, "Left Wheels");
        rightController.addShuffleboard(tab, "Right Wheels");
        drivetrainSendable = new DrivetrainSendable(leftController, rightController);
        tab.add("Drivetrain", drivetrainSendable);
    }

    public void initDefaultCommand() {
    }

    public void control(double x, double y) {
        leftController.setPercent(x);
        rightController.setPercent(y);
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

        final double epsilon = 0.0001;

        double maxInput = Math.copySign(Math.max(Math.abs(x), Math.abs(y)), (x + epsilon));

        if ((x + epsilon) * y >= 0.0) { // If both sign are the same
            control(maxInput, x - y);
        } else {
            control(x + y, maxInput);
        }
    }

    public void goForward(double x) {
        control(x, x);
    }

    public void stop() {
        control(0, 0);
    }
}


class DrivetrainSendable extends SendableBase {
    BetterTalonSRX left;
    BetterTalonSRX right;

    public DrivetrainSendable(BetterTalonSRX left, BetterTalonSRX right) {
        this.left = left;
        this.right = right;
    }

    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("DifferentialDrive");
        builder.addDoubleProperty("Left Motor Speed", left::get, left::setPercent);
        builder.addDoubleProperty("Right Motor Speed", right::get, right::setPercent);
    }
}
