package frc.robot.commands.Teleop;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class StopDrivetrain extends Command {

    public StopDrivetrain() {
        requires(Robot.myDrivetrain);
    }

    protected void initialize() {
        Robot.myDrivetrain.stop();
    }

    protected void execute() {
        Robot.myDrivetrain.stop();
    }

    protected boolean isFinished() {
        return false;
    }
}
