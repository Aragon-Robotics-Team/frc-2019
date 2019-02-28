package frc.robot.vision;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public final class Map {
    public static final Arena arena = new Arena();

    public static final Target[] targets = new Target[] {
            // rocket ports
            new Target(new Point(27.44, 229.13), Target.Height.rocket_port, 0),
            new Target(new Point(296.56, 229.13), Target.Height.rocket_port, 180),
            new Target(new Point(27.44, 418.87), Target.Height.rocket_port, 0),
            new Target(new Point(296.56, 418.87), Target.Height.rocket_port, 180),
            // closest to driverstations rocket hatches
            new Target(new Point(17.3421, 214.42), Target.Height.rocket_hatch, -60),
            new Target(new Point(306.6579, 214.42), Target.Height.rocket_hatch, -120),
            new Target(new Point(17.3421, 433.58), Target.Height.rocket_hatch, 0),
            new Target(new Point(306.6579, 433.58), Target.Height.rocket_hatch, 120),
            // furthest from driverstations rocket hatches
            new Target(new Point(17.3421, 243.84), Target.Height.rocket_hatch, 60),
            new Target(new Point(306.6579, 243.84), Target.Height.rocket_hatch, 120),
            new Target(new Point(17.3421, 404.16), Target.Height.rocket_hatch, -60),
            new Target(new Point(306.6579, 404.16), Target.Height.rocket_hatch, -120),
            // cargo ship hatches
            new Target(new Point(133.13, 260.65), Target.Height.cargo_ship_hatch, 180),
            new Target(new Point(190.87, 260.65), Target.Height.cargo_ship_hatch, 0),
            new Target(new Point(133.13, 282.4), Target.Height.cargo_ship_hatch, 180),
            new Target(new Point(190.87, 282.4), Target.Height.cargo_ship_hatch, 0),
            new Target(new Point(133.13, 304.15), Target.Height.cargo_ship_hatch, 180),
            new Target(new Point(190.87, 304.15), Target.Height.cargo_ship_hatch, 0),

            new Target(new Point(133.13, 387.35), Target.Height.cargo_ship_hatch, 180),
            new Target(new Point(190.87, 387.35), Target.Height.cargo_ship_hatch, 0),
            new Target(new Point(133.13, 365.6), Target.Height.cargo_ship_hatch, 180),
            new Target(new Point(190.87, 365.6), Target.Height.cargo_ship_hatch, 0),
            new Target(new Point(133.13, 343.85), Target.Height.cargo_ship_hatch, 180),
            new Target(new Point(190.87, 343.85), Target.Height.cargo_ship_hatch, 0),

            new Target(new Point(151.12, 220.25), Target.Height.cargo_ship_hatch, -90),
            new Target(new Point(172.88, 220.25), Target.Height.cargo_ship_hatch, -90),

            // loading station hatches
            new Target(new Point(27.44, 0), Target.Height.cargo_ship_hatch, 90),
            new Target(new Point(296.56, 0), Target.Height.cargo_ship_hatch, 90),
            new Target(new Point(27.44, 648), Target.Height.cargo_ship_hatch, -90),
            new Target(new Point(296.56, 648), Target.Height.cargo_ship_hatch, -90), };

    public final static class Arena {
        public static final String unit = "inches";
        public static final Size size = new Size(324, 648);
        public static final Point center = new Point(size.width / 2, size.height / 2);

    }

    public static class Target {
        // angle is perpendicular to target facing outwards (tape lines), relative to
        // horizontal(across short side of field) = 0 radians (+x = c-clockwise, -x =
        // clockwise )
        public Height height;
        public Point center;
        public double angle;
        public double draw_len = 12;

        public Target(Point center, Height height, double angle) {
            this.center = center;
            this.angle = angle;
            this.height = height;
        }

        public enum Height {
            rocket_port(39.125), rocket_hatch(31.5), loading_station_hatch(31.5), cargo_ship_hatch(31.5);
            private double h;

            Height(double h) {
                this.h = h;
            }
        }

        // public void draw(Mat img){
        // double max_x = img.width();
        // double max_y = img.height();
        // double dx = Math.cos(angle);
        // double dy = Math.sin(angle);
        // Point pt1 = new Point(cutoff(this.center.x + dx*draw_len, 0,max_x),
        // cutoff(this.center.y
        // + dy*draw_len,0,max_y));
        // Imgproc.line(img, pt1, center, new Scalar(255,0,0));
        // }
        // public double cutoff(double val, double min, double max){
        // return Math.max(Math.min(val,max),min);
        // }
    }

    // public void draw(Mat img){
    // for(Target t: targets){
    // t.draw(img);
    // }
    // }
}
