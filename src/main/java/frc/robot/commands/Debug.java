package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Debug extends Command {

    public Debug() {
    }

    protected void initialize() {
    }

    protected void execute() {
        Robot.myDrivetrain.refreshDashboard();
    }

    protected boolean isFinished() {
        return true; // Run never
    }
}
