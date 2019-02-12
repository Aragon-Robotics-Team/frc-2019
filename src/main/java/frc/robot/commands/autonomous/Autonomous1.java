package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Autonomous1 extends Command {

    public Autonomous1() {
        requires(Robot.myDrivetrain);
        setTimeout(5);
    }

    protected void initialize() {
    }

    protected void execute() {
        Robot.myDrivetrain.controlArcade(1, 0.5);
    }

    protected boolean isFinished() {
        return isTimedOut();
    }
}
