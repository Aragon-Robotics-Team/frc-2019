package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ResetIntakeEncoder extends Command {

    public ResetIntakeEncoder() {
    }

    protected void initialize() {
        Robot.myIntake.resetEncoder();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }
}
