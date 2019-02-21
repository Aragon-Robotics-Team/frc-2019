package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlArcadeDrivetrain extends Command {

    public ControlArcadeDrivetrain() {
        requires(Robot.myDrivetrain);
    }

    protected void initialize() {
    }

    protected void execute() {
        double speedModifier = 1.0;
        if (Robot.m_oi.getSlowMode()) {
            speedModifier = 0.7;
        }
        Robot.myDrivetrain.controlArcade(Robot.m_oi.getLeftSpeed() * speedModifier,
                Robot.m_oi.getLeftRotation() * speedModifier);
    }

    protected boolean isFinished() {
        return false;
    }
}
