package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Autonomous4 extends Command {

    public Autonomous4() {
        requires(Robot.myDrivetrain);
        setTimeout(2);
    }

    protected void initialize() {

    }

    protected void execute() {
        Robot.myDrivetrain.goForward(-1.0);
    }

    protected boolean isFinished() {
        return isTimedOut();
    }
}
