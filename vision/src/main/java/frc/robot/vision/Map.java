package frc.robot.vision;

import org.opencv.core.Point;
import org.opencv.core.Size;

public class Map {
    public static final Arena arena = new Arena();

    public static final class Arena {
        public static final String unit = "inches";
        public static final Size size = new Size(319, 649);
        public static final Point center = new Point(size.width / 2, size.height / 2);

    }
    public static final class Target {
        // angle is relative to horizontal= 0 radians (+x = c-clockwise, -x = clockwise )
        public static Point center;
        public static double angle;

        public Target(Point center, double angle) {
            this.center = center;
            this.angle = angle;
        }
    }
}
