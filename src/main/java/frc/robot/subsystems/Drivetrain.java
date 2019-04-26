package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.commands.drivetrain.IdleDrivetrain;
import frc.robot.util.BetterFollower;
import frc.robot.util.BetterFollowerConfig;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.Deadband;
import frc.robot.util.Disableable;
import frc.robot.util.SendableMaster;

public class Drivetrain extends BetterSubsystem implements BetterSendable, Disableable {
    BetterTalonSRX leftController;
    BetterTalonSRX rightController;
    BetterFollower leftSlaveController;
    BetterFollower rightSlaveController;
    SlowModes slowMode = SlowModes.Normal;

    double distance;
    double x;
    double y;

    static final double DRIVE_SPEED = 1000;

    DrivetrainSendable drivetrainSendable;

    public enum SlowModes {
        Normal(0.85, 0.1, .5), Fast(1, 0.05, .5), Slow(0.65, 0.1, 0.5 * 0.6);

        double v;
        double r;
        double t;

        private SlowModes(double v, double r, double t) {
            this.v = v;
            this.r = r;
            this.t = t;
        }
    }

    public Drivetrain() {
        var map = Robot.map.drivetrain;

        BetterTalonSRXConfig leftConfig = new BetterTalonSRXConfig();
        leftConfig.invert = map.invertLeft();
        leftConfig.invertEncoder = map.invertLeftEncoder();
        // (tick_speed for 100% output) / (max measured tick_speed)
        leftConfig.maxTickVelocity = 1112.0;
        // leftConfig.slot0.kF = (1023.0 / 1112.0) * 1.13;
        leftConfig.slot0.kP = 0.7;
        leftConfig.ticksPerInch = 76.485294;
        leftConfig.deadband = new Deadband(0.1, 0.0);
        leftController = new BetterTalonSRX(map.leftMainCanID(), leftConfig);

        BetterTalonSRXConfig rightConfig = new BetterTalonSRXConfig();
        rightConfig.invert = map.invertRight();
        rightConfig.invertEncoder = map.invertRightEncoder();
        rightConfig.maxTickVelocity = 1142.0;
        rightConfig.slot0.kP = 0.7;
        rightConfig.ticksPerInch = 76.485294;
        rightConfig.deadband = new Deadband(0.1, 0.0);
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

        stop();
        setBrake(true);
        reset();
        setSlow(SlowModes.Normal);
    }

    public void createSendable(SendableMaster master) {
        master.add("Drivetrain", drivetrainSendable);

        master.add("Left Wheels", leftController);
        master.add("Right Wheels", rightController);
    }

    public void periodic() {
        // updatePosition();
    }

    public void control(double x, double y) {
        // DesireOutput * max(liftPos) * max actual (instead of velocity PID) * swerve
        // compensate
        leftController.setOldPercent(x * slowMode.v);
        rightController.setOldPercent(y * slowMode.v);
    }

    public void controlRaw(double x, double y) {
        leftController.setOldPercent(x);
        rightController.setOldPercent(y);
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

        y *= slowMode.t;

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

    public void setSlow(SlowModes slowMode) {
        this.slowMode = slowMode;

        leftController.setOpenLoopRamp(slowMode.r);
        rightController.setOpenLoopRamp(slowMode.r);

        System.out.println("SlowMode: " + slowMode.v + " " + slowMode.r + " " + slowMode.t);
    }

    public void updatePosition() {
        double leftPos = leftController.getInch();
        double rightPos = rightController.getInch();
        double newDistance = (leftPos + rightPos) / 2;

        double angle = Robot.myNavX.getYaw();

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

    public void disable() {
        setBrake(true);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new IdleDrivetrain());
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
        builder.addDoubleProperty("Left Motor Speed", left::getDesired, null);
        builder.addDoubleProperty("Right Motor Speed", right::getDesired, null);
    }
}
