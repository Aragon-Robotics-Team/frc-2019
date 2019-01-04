package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Autonomous3 extends Command {

    public Autonomous3() {
        requires(Robot.myDrivetrain);
        setTimeout(3);
    }

    protected void initialize() {

    }

    protected void execute() {
        Robot.myDrivetrain.stop();
    }

    protected boolean isFinished() {
        return isTimedOut();
    }
}
