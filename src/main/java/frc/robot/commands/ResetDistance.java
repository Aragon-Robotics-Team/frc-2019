package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ResetDistance extends Command {

    public ResetDistance() {
        //requires(Robot.myDrivetrain);
    }

    protected void initialize() {
        Robot.myDrivetrain.resetDistance();
    }

    protected void execute() {
        
    }

    protected boolean isFinished() {
        return true; // Run never
    }
}
