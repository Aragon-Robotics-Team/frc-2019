package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlCheezyDrivetrain extends Command {

    public ControlCheezyDrivetrain() {
        requires(Robot.myDrivetrain);
    }

    protected void execute() {
        Robot.myDrivetrain.controlCheezy(Robot.map.oi.getLeftSpeed(),
                Robot.map.oi.getLeftRotation(), Robot.map.oi.getSlowMode());
    }

    protected boolean isFinished() {
        return false;
    }
}
