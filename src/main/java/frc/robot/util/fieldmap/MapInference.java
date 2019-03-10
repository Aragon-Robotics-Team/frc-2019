package frc.robot.util.fieldmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.opencv.core.Point;
import frc.robot.util.fieldmap.Map.Target;
import frc.robot.vision.CoordTransform;

public class MapInference {
    public static Target[] getClosestTargets(Point robot_location, double target_direction) {
        // target direction is in degrees, (-180,180), counterclockwise is positive,
        // zero is horizontal
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
}
