package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlArcadeDrivetrain extends Command {

    public ControlArcadeDrivetrain() {
        requires(Robot.myDrivetrain);
    }

    protected void execute() {
        Robot.myDrivetrain.controlArcade(Robot.map.oi.getLeftSpeed(),
                Robot.map.oi.getLeftRotation());
    }

    protected boolean isFinished() {
        return false;
    }
}
