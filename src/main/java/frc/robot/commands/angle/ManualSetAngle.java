package frc.robot.commands.angle;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ManualSetAngle extends Command {
    double angle;

    public ManualSetAngle() {
        requires(Robot.myAngle);
        requires(Robot.myDrivetrain);
    }

    protected void initialize() {
        end();
        Robot.myAngle.setAngle(Robot.myAngle.entry.getNumber(0.0).doubleValue());
    }

    protected void execute() {
        Robot.myAngle.setEnabled(true);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        Robot.myAngle.disableAndReset();
    }
}
