package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.commands.angle.ManualSetAngle;
import frc.robot.commands.angle.ResetAngle;
import frc.robot.commands.autonomous.AutoAlign;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.Disableable;
import frc.robot.util.SendableMaster;

public class TurnToAngle extends BetterSubsystem implements PIDOutput, BetterSendable, Disableable {
    public double currentAngle;
    public double pidOut;
    public boolean enabled = false;

    // Constants are as of 04/25/2019. No oscilation, little overshoot,
    // deadband(0.1, 0), no velocity PID, ~5 degrees for delta angles > 30,
    // bad at angles ~~ 15
    static final double kP = 0.005;
    static final double kI = 0.0;
    static final double kD = 0.001;
    static final double kF = 0.0;

    static final double kToleranceDegrees = 0.0f;

    PIDController turnController;
    AngleSendable sendable;
    public NetworkTableEntry entry;

    public TurnToAngle() {
        turnController = new PIDController(kP, kI, kD, kF, Robot.myNavX.getAHRS(), this);
        turnController.setInputRange(-180.0f, 180.0f);
        turnController.setOutputRange(-1.0, 1.0);
        turnController.setAbsoluteTolerance(kToleranceDegrees);
        turnController.setContinuous(true);
        turnController.disable();

        sendable = new AngleSendable(this);
    }

    public String getTabName() {
        return "Angle";
    }

    public void createSendable(SendableMaster master) {
        master.add(sendable);
        master.add(turnController);
        master.add(new ResetAngle());
        master.add(new AutoAlign());
        master.add(new ManualSetAngle());
        entry = Shuffleboard.getTab("123").add("Angle", 0.0).getEntry();
    }

    public void periodic() {
        if (Robot.myNavX.isRunning() && enabled) { // && !turnController.onTarget()) {
            turnController.enable();
        } else {
            turnController.disable();
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getActualAngle() {
        return Robot.myNavX.getYaw();
    }

    public void setAngle(double angle) {
        currentAngle = angle;
        turnController.setSetpoint(angle);
    }

    public void disableAndReset() {
        enabled = false;
        reset();
    }

    public void reset() {
        turnController.reset();
    }

    public void disable() {
        disableAndReset();
    }

    public boolean isOnTarget() {
        return (!enabled || turnController.onTarget());
    }

    public void pidWrite(double output) {
        pidOut = output;
        // Robot.myDrivetrain.controlArcade(0, output);
        Robot.myDrivetrain.controlRaw(output, -output);
    }
}


class AngleSendable extends SendableBase {
    TurnToAngle angle;

    public AngleSendable(TurnToAngle angle) {
        this.angle = angle;
    }

    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Set Angle", () -> angle.currentAngle, null);
        builder.addDoubleProperty("Diff Angle",
                () -> (Robot.myNavX.getYaw() - angle.currentAngle + 180) % 360 - 180, null);
        builder.addDoubleProperty("PID Out", () -> angle.pidOut, null);
    }
}
