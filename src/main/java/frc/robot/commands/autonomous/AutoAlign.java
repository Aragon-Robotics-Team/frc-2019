package frc.robot.commands.autonomous;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.ByteArrayInput;

public class AutoAlign extends Command {
    boolean enabled;
    double lastAngle;
    Instant lastTimeStamp;
    Clock clock = Clock.systemUTC();

    public ArrayList<Pose> pose_history = new ArrayList<Pose>();

    public AutoAlign() {
        requires(Robot.myDrivetrain); // This will exit ControlArcadeDrivetrain
        requires(Robot.myAngle);
        setTimeout(5);
        SmartDashboard.putNumber("Wanted Angle Vision", 0);
    }

    protected void initialize() {
        enabled = false;
        lastAngle = -360; // Impossible value
        lastTimeStamp = clock.instant().minus(1, ChronoUnit.DAYS);
        Robot.myAngle.disableAndReset();

    }

    protected void execute() {
        double[] angles = ByteArrayInput.getNetworkObject(new double[0], "table", "target_offsets");

        if (angles.length != 0) {
            enabled = true;
            double angle = -1 * Math.toDegrees(angles[0]);

            SmartDashboard.putNumber("Wanted Angle Vision", angle);

            if (angle != lastAngle) { // Replace with test for new data
                Robot.myAngle.setDeltaAngle(angle);
                lastAngle = angle;
            }
        } else {
            enabled = false;
        }

        Robot.myAngle.setEnabled(enabled);
    }

    public class Pose {
        public Instant t;
        public double angle;

        public Pose(Instant t, double angle) {
            this.t = t;
            this.angle = angle;
        }
    }

    protected void execute_experimental() {
        pose_history.add(new Pose(clock.instant(), Robot.myNavX.ahrs.getAngle()));
        if (pose_history.size() > 256) {
            pose_history.remove(pose_history.size() - 1);
        }

        double[] angles = ByteArrayInput.getNetworkObject(new double[0], "table", "target_offsets");
        Instant timeStamp = clock.instant();
        timeStamp = ByteArrayInput.getNetworkObject(timeStamp, "table", "timestamp");
        Pose closest_pose;

        if (angles.length != 0) {
            enabled = true;
            double angle = angles[0];

            if (angle != lastAngle && timeStamp != lastTimeStamp) { // Replace with test for new
                                                                    // data

                // modified binary search
                // David Soroko, May 14 '15 at 19:09, accessed 3/2/2019
                // https://stackoverflow.com/questions/30245166/find-the-nearest-closest-value-in-a-sorted-list
                if (timeStamp.isBefore(pose_history.get(0).t)) {
                    closest_pose = pose_history.get(0);
                } else if (timeStamp.isAfter(pose_history.get(pose_history.size() - 1).t)) {
                    closest_pose = pose_history.get(pose_history.size() - 1);
                } else {

                    int lo = 0;
                    int hi = pose_history.size();

                    while (lo <= hi) {
                        int mid = (hi + lo) / 2;

                        if (timeStamp.isBefore(pose_history.get(mid).t)) {
                            hi = mid - 1;
                        } else if (timeStamp.isAfter(pose_history.get(mid).t)) {
                            lo = mid + 1;
                        } else {
                            closest_pose = pose_history.get(mid);
                        }
                    }
                    closest_pose = pose_history.get(lo).t.getNano()
                            - timeStamp.getNano() < timeStamp.getNano()
                                    - pose_history.get(hi).t.getNano() ? pose_history.get(lo)
                                            : pose_history.get(hi);
                }

                double angle_change_est = Robot.myNavX.ahrs.getAngle() - closest_pose.angle;
                Robot.myAngle.setDeltaAngle(angle + angle_change_est);
                lastAngle = angle;
                lastTimeStamp = timeStamp;
            }
        } else {
            enabled = false;
        }

    }

    protected void end() {
        Robot.myAngle.disableAndReset();
    }

    protected boolean isFinished() {
        return isTimedOut();
    }
}
