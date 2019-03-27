package frc.robot.commands.angle;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TurnCardinal extends Command {
    public static final double[] angles =
            new double[] {-118.75, -90, -61.25, 0, 61.25, 90, 118.75, 180};
    public static double target = 0;
    public static int target_index = -1;

    public TurnCardinal(boolean direction) {
        requires(Robot.myDrivetrain);
        requires(Robot.myAngle);
        if (target_index == -1) {
            target = getClosestAngle(direction);
            Robot.myAngle.setAngle(target);
            Robot.myAngle.enable();
        } else {
            target = getNextAngle(direction);
            Robot.myAngle.setAngle(target);
        }
    }

    public static double getClosestAngle(boolean direction) { // if True: round down (left)
        int low = 0;
        int high = angles.length - 1;
        int middle;
        double current_angle = Robot.myNavX.getYaw();
        while (low <= high) {
            middle = (low + high) / 2;
            if (angles[middle] < current_angle) {
                low = middle + 1;
            } else if (angles[middle] == current_angle) {
                return angles[middle];
            } else {
                high = middle - 1;
            }
        }
        if (direction) {
            return angles[high];
        } else {
            return angles[low];
        }
    }

    double getNextAngle(boolean direction) {
        if (direction) {
            target_index--;
            return angles[target_index];
        } else {
            target_index++;
            return angles[target_index];
        }
    }

    protected void end() {
        target_index = -1;
        Robot.myAngle.disable();
    }

    protected boolean isFinished() {
        return Robot.myAngle.isOnTarget();
    }
}
