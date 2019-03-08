package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.commands.drivetrain.ResetDrivetrainLocator;
import frc.robot.commands.drivetrain.SetBrakeMode;
import frc.robot.util.BetterFollower;
import frc.robot.util.BetterFollowerConfig;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.SendableMaster;

public class Drivetrain extends Subsystem implements BetterSendable {
    BetterTalonSRX leftController;
    BetterTalonSRX rightController;
    BetterFollower leftSlaveController;
    BetterFollower rightSlaveController;

    double distance;
    double x;
    double y;

    DrivetrainSendable drivetrainSendable;

    public Drivetrain() {
        var map = Robot.map.drivetrain;

        BetterTalonSRXConfig leftConfig = new BetterTalonSRXConfig();
        leftConfig.invert = false;
        // (tick_speed for 100% output) / (max measured tick_speed)
        leftConfig.maxTickVelocity = 1112.0;
        leftConfig.slot0.kF = (1023.0 / 1112.0) * 1.13;
        leftConfig.slot0.kP = 0.7;
        leftConfig.ticksPerInch = 76.485294;
        leftController = new BetterTalonSRX(map.leftMainCanID(), leftConfig);

        BetterTalonSRXConfig rightConfig = new BetterTalonSRXConfig();
        rightConfig.invert = true;
        rightConfig.maxTickVelocity = 1142.0;
        rightConfig.slot0.kP = 0.7;
        rightConfig.ticksPerInch = 76.485294;
        rightController = new BetterTalonSRX(map.rightMainCanID(), rightConfig);

        BetterFollowerConfig leftSlaveConfig = new BetterFollowerConfig();
        leftSlaveConfig.controller = map.slaveController();
        leftSlaveConfig.invert = false;
        leftSlaveController = new BetterFollower(map.leftSlaveCanID(), leftSlaveConfig);
        leftController.addFollower(leftSlaveController);

        BetterFollowerConfig rightSlaveConfig = new BetterFollowerConfig();
        rightSlaveConfig.controller = map.slaveController();
        rightSlaveConfig.invert = false;
        rightSlaveController = new BetterFollower(map.rightSlaveCanID(), rightSlaveConfig);
        rightController.addFollower(rightSlaveController);

        drivetrainSendable = new DrivetrainSendable(this);
        reset();
    }

    public void initDefaultCommand() {
    }

    public void createSendable(SendableMaster master) {
        master.add("Drivetrain", drivetrainSendable);

        master.add("Left Wheels", leftController);
        master.add("Right Wheels", rightController);

        master.add(new ResetDrivetrainLocator());
        master.add("Brake", new SetBrakeMode(true));
        master.add("Coast", new SetBrakeMode(false));
        master.add("Reset Encoder", new InstantCommand(this::resetEncoders));
        master.add("Reset Position", new InstantCommand(this::reset));
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
        double leftPos = leftController.getInch();
        double rightPos = rightController.getInch();
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
