package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.ByteArrayInput;

public class AutoAlign extends Command {
    public int state; // 0-stopped, 1-turning
    public double[] last_angles;

    public AutoAlign() {
        requires(Robot.myDrivetrain);
        requires(Robot.myAngle);
        setTimeout(5);
    }

    protected void initialize() {
    }

    protected void execute() {
        double[] angles = ByteArrayInput.getNetworkObject(new double[0], "table", "target_offsets");
        if (angles.length != 0) {
            if (angles[0] != last_angles[0]) {
                double targetAngle = angles[0] + Robot.myNavX.ahrs.getAngle();
                Robot.myAngle.setAngle(targetAngle);
                last_angles = angles;
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
        return isTimedOut();
    }

}
