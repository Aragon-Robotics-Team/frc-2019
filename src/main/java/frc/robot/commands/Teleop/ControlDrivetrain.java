package frc.robot.commands.Teleop;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlDrivetrain extends Command {

    public ControlDrivetrain() {
        requires(Robot.myDrivetrain);
    }

    protected void initialize() {
        Robot.myDrivetrain.resetDistance();
    }

    protected void execute() {
        Robot.myDrivetrain.control(Robot.m_oi.getLeftSpeed(), Robot.m_oi.getRightSpeed());
    }

    protected boolean isFinished() {
        return false; // Run forever
    }
}
