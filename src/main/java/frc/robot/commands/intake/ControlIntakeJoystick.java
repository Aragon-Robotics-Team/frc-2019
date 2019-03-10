package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlIntakeJoystick extends Command {
    protected void execute() {
        Robot.myIntake.controller.setOldPercent(Robot.map.oi.getRightSpeed() * 0.2);
    }

    protected boolean isFinished() {
        return false;
    }
}
