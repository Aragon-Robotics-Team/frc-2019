package frc.robot.commands.Teleop;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlAngle extends Command {
    public ControlAngle() {
        // requires(Robot.myDrivetrain);
        // requires(Robot.myAngle);
    }

    protected void initialize() {
    }

    protected void execute() {
        Robot.myAngle.setAngle(Robot.m_oi.getAngle());
    }

    protected boolean isFinished() {
        return false;
    }
}
