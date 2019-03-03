package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class TurnToAngle extends Subsystem implements PIDOutput {
    public double currentAngle;
    public double pidOut;
    public boolean enabled = false;

    static final double kP = 0.005;
    static final double kI = 0;
    static final double kD = 0;
    static final double kF = 0;

    static final double kToleranceDegrees = 1.5f;

    PIDController turnController;

    public TurnToAngle() {
        turnController = new PIDController(kP, kI, kD, kF, Robot.myNavX.ahrs, this);
        turnController.setInputRange(-180.0f, 180.0f);
        turnController.setOutputRange(-1.0, 1.0);
        turnController.setAbsoluteTolerance(kToleranceDegrees);
        turnController.setContinuous(true);
        turnController.disable();

        var tab = Shuffleboard.getTab("Vison");
        tab.add(turnController);
    }

    public void periodic() {
        SmartDashboard.putNumber("Set Angle", currentAngle);
        SmartDashboard.putNumber("NavX Angle", getActualAngle());
        SmartDashboard.putNumber("Diff Angle", (getActualAngle() - currentAngle + 180) % 360 - 180);
        SmartDashboard.putNumber("PID Out", pidOut);
        SmartDashboard.putBoolean("NavX Enable", Robot.myNavX.isRunning());

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
