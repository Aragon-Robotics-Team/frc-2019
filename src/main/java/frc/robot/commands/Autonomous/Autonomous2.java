package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Autonomous2 extends Command {

    public Autonomous2() {
        requires(Robot.myDrivetrain);
        setTimeout(3);
    }

    protected void initialize() {
        
    }

    protected void execute() {
        Robot.myDrivetrain.goForward(.5);
    }

    protected boolean isFinished() {
        return isTimedOut();
    }
}
