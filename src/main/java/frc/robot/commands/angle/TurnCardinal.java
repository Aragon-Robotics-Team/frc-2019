package frc.robot.commands.angle;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TurnCardinal extends Command {
    public static final double[] angles = new double[] { -118.75, -90, -61.25, 0, 61.25, 90, 118.75, 180 };
    public static double target = 0;
    public static int target_index = -1;
    public static double tollerance = .5; // tollerance degrees

    public TurnCardinal(boolean direction) {
        // requires(Robot.myDrivetrain);
        // if (target_index == -1) {
        // target = getClosestAngle(direction);
        // Robot.myAngle.setAngle(target);
        // Robot.myAngle.enable();
        // } else {
        // target = getNextAngle(direction);
        // Robot.myAngle.setAngle(target);
        // }
    }

    public double getClosestAngle(boolean direction) {
        int l = 0;
        int h = angles.length - 1;
        int m;
        double current_angle = Robot.myNavX.ahrs.getAngle();
        while (l <= h) {
            m = (l + h) / 2;
            if (angles[m] < current_angle) {
                l = m + 1;
            } else if (angles[m] == current_angle) {
                return angles[m];
            } else {
                h = m - 1;
            }
        }
        if (direction == true) {
            return angles[h];
        } else {
            return angles[l];
        }
    }

    public double getNextAngle(boolean direction) {
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
        // return (!Robot.myAngle.enabled
        // || equalsWithTollerance(Robot.myNavX.ahrs.getAngle(), target));
        return true;
    }

    public boolean equalsWithTollerance(double angle1, double angle2) {
        return tollerance > Math.abs(angle1 - angle2);
    }
}
