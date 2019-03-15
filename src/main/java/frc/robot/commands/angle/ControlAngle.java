package frc.robot.commands.angle;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlAngle extends Command {
    protected void execute() {
        Robot.myAngle.setAngle(Robot.map.oi.getAngle());
    }

    protected boolean isFinished() {
        return false;
    }
}
