package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlLiftJoystick extends Command {
    public ControlLiftJoystick() {
        requires(Robot.myLift);
    }

    protected void execute() {
        Robot.myLift.controller.setOldPercent(Robot.map.oi.getRightSpeed());
    }

    protected boolean isFinished() {
        return false;
    }
}
