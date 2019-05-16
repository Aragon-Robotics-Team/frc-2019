package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class AutoAlign extends Command {
    boolean useTimer = true;

    public AutoAlign() {
        requires(Robot.myDrivetrain);
        requires(Robot.myAngle);
        setTimeout(5);
    }

    public AutoAlign(boolean useTimer) {
        this();
        this.useTimer = useTimer;
    }

    protected void initialize() {
        end();
    }

    protected void execute() {
        if (Robot.myVision.seeTarget) {
            double yaw = Robot.myVision.calculatedYaw;
            Robot.myAngle.setAngleOffset(yaw);
            Robot.myAngle.setEnabled(true);
        }
    }

    protected boolean isFinished() {
        return (useTimer && isTimedOut());
    }

    protected void end() {
        Robot.myAngle.disableAndReset();
    }
}
