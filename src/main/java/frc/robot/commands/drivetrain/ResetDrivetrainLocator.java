package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ResetDrivetrainLocator extends Command {
    public ResetDrivetrainLocator() {
    }

    protected void initialize() {
        Robot.myDrivetrain.reset();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }
}
