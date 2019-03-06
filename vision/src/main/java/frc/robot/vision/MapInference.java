package frc.robot.vision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.opencv.core.Point;
import frc.robot.vision.Map.Target;

public class MapInference {
    public static Target[] getClosestTargets(Point robot_location, double target_direction) {
        List<Target> targets = new ArrayList<Target>(Arrays.asList(Map.targets));
        // todo filtering by line of sight
        Target[] targets_array = new Target[targets.size()];
        targets_array = targets.toArray(targets_array);
        Arrays.sort(targets_array, Comparator.comparingDouble(
                (Target t) -> Math.abs(target_direction - CoordTransform.toPolar(new double[] {
                        t.center.x - robot_location.x, t.center.y - robot_location.y})[1])));

        for (int i = 0; i < targets.size(); i++) {
            double opposite = target_direction + 180;
            if (opposite >= 180) {
                opposite -= 360;
            }

            if (targets.get(i).angle < target_direction + 90
                    && targets.get(i).angle > target_direction - 90) {
                targets.remove(i);
                i -= 1;
            }
        }

        return targets_array;
    }

}
