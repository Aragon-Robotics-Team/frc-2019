package frc.robot.util.fieldmap.Geometry;

import org.opencv.core.Point;

public class Line {
    public double slope;
    public double y_intercept;

    public Line(Point p1, Point p2) {
        if (p1.x - p2.x != 0) {
            this.slope = (p1.y - p2.y) / (p1.x - p2.x);
        } else {
            this.slope = Double.POSITIVE_INFINITY;
        }
        this.y_intercept = p1.y - p1.x * slope;

    }

    public Line(Point p1, double slope) {
        this.slope = slope;
        this.y_intercept = p1.y - p1.x * slope;
    }

    public Line(double y_intercept, double slope) {
        this.y_intercept = y_intercept;
        this.slope = slope;
    }

    public Point getIntercept(Line l2) {
        double x = (l2.y_intercept - y_intercept) / (slope - l2.slope);
        double y = slope * x + y_intercept;
        return new Point(x, y);
    }

    public Line getParallelLine(Point p) {
        // get parallel line that intersects point p
        return new Line(p, slope);
    }
}