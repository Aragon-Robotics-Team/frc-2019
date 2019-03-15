package frc.robot.util.fieldmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.opencv.core.Point;
import frc.robot.util.fieldmap.geometry.Line;
import frc.robot.subsystems.Vision.VisionPositioningServices.PoseHistory.Pose;
import frc.robot.util.fieldmap.Map.Target;
import frc.robot.vision.CoordTransform;

public class MapInference {
    public static Target[] get_closest_targets_by_angle(Point robot_location, double target_direction) {
        // target direction is in degrees, (-180,180), counterclockwise is positive,
        // zero is horizontal

        // filter out non-visible targets
        List<Target> targets = new ArrayList<Target>(Arrays.asList(Map.targets));
        for (int i = 0; i < targets.size(); i++) {
            double angle_diff = targets.get(i).angle - target_direction;
            angle_diff = Math.abs((angle_diff + 180) % 360 - 180);
            if (angle_diff < 90) {
                targets.remove(i);
                i--;
            }
        }

        Target[] targets_array = new Target[targets.size()];
        targets_array = targets.toArray(targets_array);
        Arrays.sort(targets_array, Comparator.comparingDouble((Target t) -> Math.abs(target_direction - CoordTransform
                .toPolar(new double[] { t.center.x - robot_location.x, t.center.y - robot_location.y })[1])));
        return targets_array;
    }

    public static Target[] get_closest_targets_by_position(Point p) {
        Target[] targets_array = Map.targets.clone();
        Arrays.sort(targets_array, Comparator.comparingDouble((Target t) -> {
            return Math.pow(Math.pow(t.center.x - p.x, 2) + Math.pow(t.center.y - p.y, 2), 0.5);
        }));
        return targets_array;
    }

    public static Point getPos(Point robot_location_approx, double target_direction, Target target) {
        Line l1 = new Line(robot_location_approx, Math.tan(target_direction * Math.PI / 180))
                .getParallelLine(target.center);
        Line l2 = new Line(robot_location_approx, Math.tan((target_direction * Math.PI / 180 + Math.PI / 2)));

        return l1.getIntercept(l2);
    }

}
