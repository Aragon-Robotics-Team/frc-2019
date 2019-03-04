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
        if (Robot.map.oi.getSlowMode()) {
            speedModifier = 0.7;
        }
        Robot.myDrivetrain.controlArcade(Robot.map.oi.getLeftSpeed() * speedModifier,
                Robot.map.oi.getLeftRotation() * speedModifier);
    }

    protected boolean isFinished() {
        return false;
    }
}
