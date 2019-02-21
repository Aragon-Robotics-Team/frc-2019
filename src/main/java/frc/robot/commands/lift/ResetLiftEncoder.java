package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ResetLiftEncoder extends Command {

    public ResetLiftEncoder() {
    }

    protected void initialize() {
        Robot.myLift.resetEncoder();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }
}
