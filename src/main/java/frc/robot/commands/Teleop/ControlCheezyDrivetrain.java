package frc.robot.commands.Teleop;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlCheezyDrivetrain extends Command {

    public ControlCheezyDrivetrain() {
        requires(Robot.myDrivetrain);
    }

    protected void initialize() {
        Robot.myDrivetrain.resetDistance();
    }

    protected void execute() {
        Robot.myDrivetrain.controlCheezy(Robot.m_oi.getLeftSpeed(), Robot.m_oi.getRightSpeed(),
                Robot.m_oi.getSlowMode());
    }

    protected boolean isFinished() {
        return false; // Run forever
    }
}
