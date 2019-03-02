package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.drivetrain.ResetDrivetrainLocator;
import frc.robot.commands.drivetrain.SetBrakeMode;
import frc.robot.util.BetterFollower;
import frc.robot.util.BetterFollowerConfig;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;

public class Drivetrain extends Subsystem {
    BetterTalonSRX leftController;
    BetterTalonSRX rightController;
    BetterFollower leftSlaveController;
    BetterFollower rightSlaveController;

    double distance;
    double x;
    double y;

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

        BetterFollowerConfig leftSlaveConfig = new BetterFollowerConfig();
        leftSlaveConfig.isConnected = RobotMap.DRIVETRAIN_LEFT_SLAVE_INSTALLED;
        leftSlaveConfig.controller = RobotMap.DRIVETRAIN_SLAVE_CONTROLLER;
        leftSlaveConfig.invert = false;
        leftSlaveController =
                new BetterFollower(RobotMap.DRIVETRAIN_LEFT_SLAVE_CAN, leftSlaveConfig);
        leftController.addFollower(leftSlaveController);

        BetterFollowerConfig rightSlaveConfig = new BetterFollowerConfig();
        rightSlaveConfig.isConnected = RobotMap.DRIVETRAIN_RIGHT_SLAVE_INSTALLED;
        rightSlaveConfig.controller = RobotMap.DRIVETRAIN_SLAVE_CONTROLLER;
        rightSlaveConfig.invert = false;
        rightSlaveController =
                new BetterFollower(RobotMap.DRIVETRAIN_RIGHT_SLAVE_CAN, rightSlaveConfig);
        rightController.addFollower(rightSlaveController);

        tab = Shuffleboard.getTab("Drive");
        leftController.addShuffleboard(tab, "Left Wheels");
        rightController.addShuffleboard(tab, "Right Wheels");
        drivetrainSendable = new DrivetrainSendable(this);
        tab.add("Drivetrain", drivetrainSendable);
        tab.add(new ResetDrivetrainLocator());
        tab.add("Brake", new SetBrakeMode(true));
        tab.add("Coast", new SetBrakeMode(false));
        tab.add("Reset Encoder", new InstantCommand(this::resetEncoders));
        tab.add("Reset Position", new InstantCommand(this::reset));

        reset();
    }

    public void initDefaultCommand() {
    }

    public void periodic() {
        updatePosition();
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

    public void setBrake(boolean brake) {
        leftController.setBrakeMode(brake);
        rightController.setBrakeMode(brake);
    }

    public void goForward(double x) {
        control(x, x);
    }

    public void stop() {
        control(0, 0);
    }

    public void updatePosition() {
        double leftPos = leftController.getEncoderPos();
        double rightPos = rightController.getEncoderPos();
        double newDistance = (leftPos + rightPos) / 2;

        double angle = Robot.myNavX.ahrs.getYaw();

        rawUpdatePosition(newDistance, angle);
    }

    // angle is in degrees
    public void rawUpdatePosition(double newDistance, double angle) {
        double deltaDistance = newDistance - distance;
        angle = Math.toRadians(angle);

        x += deltaDistance * Math.cos(angle);
        y += deltaDistance * Math.sin(angle);

        distance = newDistance;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void reset() {
        distance = 0;
        x = 0;
        y = 0;
    }

    public void resetEncoders() {
        leftController.resetEncoder();
        rightController.resetEncoder();
    }
}


class DrivetrainSendable extends SendableBase {
    Drivetrain drivetrain;
    BetterTalonSRX left;
    BetterTalonSRX right;

    public DrivetrainSendable(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        this.left = drivetrain.leftController;
        this.right = drivetrain.rightController;
    }

    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("DifferentialDrive");
        builder.addDoubleProperty("Left Motor Speed", left::get, left::setPercent);
        builder.addDoubleProperty("Right Motor Speed", right::get, right::setPercent);
        builder.addDoubleProperty("X", drivetrain::getX, null);
        builder.addDoubleProperty("Y", drivetrain::getY, null);
    }
}
