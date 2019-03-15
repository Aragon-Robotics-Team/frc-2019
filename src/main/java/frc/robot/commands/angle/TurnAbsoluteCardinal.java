package frc.robot.commands.angle;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TurnAbsoluteCardinal extends Command {
    public TurnAbsoluteCardinal() {
        requires(Robot.myAngle);
    }

    protected void initialize() {
        setTimeout(2);
        Integer pov = Robot.map.oi.getPOV();
        if (pov != null) {
            double angle = TurnCardinal.angles[pov];
            Robot.myAngle.setAngle(angle);
            Robot.myAngle.setEnabled(true);
        }
    }

    protected void end() {
        Robot.myAngle.disable();
    }

    protected boolean isFinished() {
        return isTimedOut();
    }
}
