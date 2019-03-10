package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.util.BetterSendable;
import frc.robot.util.SendableMaster;

public class TurnToAngle extends Subsystem implements PIDOutput, BetterSendable {
    public double currentAngle;
    public double pidOut;
    public boolean enabled = false;

    static final double kP = 0.005;
    static final double kI = 0;
    static final double kD = 0;
    static final double kF = 0;

    static final double kToleranceDegrees = 1.5f;

    PIDController turnController;
    AngleSendable sendable;

    public TurnToAngle() {
        turnController = new PIDController(kP, kI, kD, kF, Robot.myNavX.ahrs, this);
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
        master.add("TurnToAngle", sendable);
    }

    public void periodic() {
        if (Robot.myNavX.isRunning() && enabled) {// && !turnController.onTarget()) {
            turnController.enable();
        } else {
            turnController.disable();
        }
    }

    public double getActualAngle() {
        return Robot.myNavX.ahrs.getYaw();
    }

    public void setAngle(double angle) {
        currentAngle = angle;
        turnController.setSetpoint(angle);
    }

    public void disableAndReset() {
        enabled = false;
        reset();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void reset() {
        turnController.reset();
    }

    public void setDeltaAngle(double angle) {
        setAngle(angle + getActualAngle());
    }

    public void initDefaultCommand() {
    }

    public void pidWrite(double output) {
        pidOut = output;
        Robot.myDrivetrain.controlArcade(0, -output);
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
                () -> (Robot.myNavX.ahrs.getYaw() - angle.currentAngle + 180) % 360 - 180, null);
        builder.addDoubleProperty("PID Out", () -> angle.pidOut, null);
    }
}
