package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlLiftJoystick extends Command {

    public ControlLiftJoystick() {
        requires(Robot.myLift);
    }

    protected void initialize() {
    }

    protected void execute() {
        Robot.myLift.setVelocity(Robot.m_oi.getLeftSpeed());
    }

    protected boolean isFinished() {
        return false;
    }
}
