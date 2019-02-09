package frc.robot.commands.angle;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetAngle extends Command {
    double angle;

    public SetAngle(double angle) {
        this.angle = angle;
    }

    protected void initialize() {
        Robot.myAngle.setAngle(angle);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }
}
