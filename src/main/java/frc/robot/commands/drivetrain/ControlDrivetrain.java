package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlDrivetrain extends Command {

    public ControlDrivetrain() {
        requires(Robot.myDrivetrain);
    }

    protected void initialize() {
    }

    protected void execute() {
        Robot.myDrivetrain.control(Robot.map.oi.getLeftSpeed(), Robot.map.oi.getRightSpeed());
    }

    protected boolean isFinished() {
        return false;
    }
}
