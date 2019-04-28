package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class AutoAlign extends Command {
    public int state; // 0-stopped, 1-turning
    public double lastAngle;

    public AutoAlign() {
        requires(Robot.myDrivetrain);
        requires(Robot.myAngle);
    }

    protected void initialize() {
        end();
    }

    protected void execute() {
        if (Robot.myVision.seeTarget) {
            double yaw = Robot.myVision.calculatedYaw;
            if (yaw != lastAngle) {
                lastAngle = yaw;
                Robot.myAngle.setAngleOffset(yaw);
            }
            if (state == 0) {
                state = 1;
                Robot.myAngle.setEnabled(true);
            }
        } else {
            if (state == 1) {
                state = 0;
                Robot.myAngle.setEnabled(false);
            }
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        Robot.myAngle.disableAndReset();
    }
}
