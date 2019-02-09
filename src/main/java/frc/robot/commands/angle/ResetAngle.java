package frc.robot.commands.angle;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ResetAngle extends Command {
    public ResetAngle() {
    }

    protected void initialize() {
        setTimeout(1);
    }

    protected void execute() {
        Robot.myAngle.reset();
        Robot.myNavX.zeroYaw();
        Robot.myAngle.setAngle(0);
    }

    protected boolean isFinished() {
        return isTimedOut();
    }
}
