package frc.robot.commands.Teleop;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetAngle extends Command {
    double angle;

    boolean m_runWhenDisabled = true;

    public SetAngle(double angle) {
        // requires(Robot.myDrivetrain);
        // requires(Robot.myAngle);
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
