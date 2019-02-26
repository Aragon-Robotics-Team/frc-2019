package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.ByteArrayInput;

public class AutoAlign extends Command {
    boolean enabled;
    double lastAngle;

    public AutoAlign() {
        requires(Robot.myDrivetrain); // This will exit ControlArcadeDrivetrain
        requires(Robot.myAngle);
        setTimeout(5);
    }

    protected void initialize() {
        enabled = false;
        lastAngle = -360; // Impossible value
        Robot.myAngle.disableAndReset();
    }

    protected void execute() {
        double[] angles = ByteArrayInput.getNetworkObject(new double[0], "table", "target_offsets");

        if (angles.length != 0) {
            enabled = true;
            double angle = angles[0];

            if (angle != lastAngle) { // Replace with test for new data
                Robot.myAngle.setDeltaAngle(angle);
                lastAngle = angle;
            }
        } else {
            enabled = false;
        }

        Robot.myAngle.setEnabled(enabled);
    }

    protected void end() {
        Robot.myAngle.disableAndReset();
    }

    protected boolean isFinished() {
        return isTimedOut();
    }
}
