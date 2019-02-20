package frc.robot.vision;

import java.lang.Math;

public class CoordTransform {
    public static double h_fov = 1.0418368970654722554442559253234; // ~61 * 68.5/70 degrees
    public static double v_fov = 0.5858197634318967224944368064432; // ~34.3 * 68.5/70 degrees
    public static double img_width = 320;
    public static double img_height = 180;
    public static double h_angle_pixel_ratio = h_fov / img_height;
    public static double v_angle_pixel_ratio = v_fov / img_width;

    public static double toRadians(double degrees) {
        return Math.PI * degrees / 180;
    }

    public static double toDegrees(double radians) {
        return radians * 180 / Math.PI;
    }

    public static double getDistance(double refAngle, double refDist, double currentAngle) {
        // radian angles and arbitrary unit distances
        // double length = refDist*(Math.tan(refAngle/2)*2); // length of object
        // double actualDist = length/(Math.tan(currentAngle/2)*2);

        return (refDist * (Math.tan(refAngle / 2) * 2)) / (Math.tan(currentAngle / 2) * 2);
    }

    public static double[] transform(double AngleX, double AngleY, double Distance) {

        double coordinates[] = new double[3];
        double x = Distance * Math.sin(Math.toRadians(AngleY)) * Math.cos(Math.toRadians(AngleX));
        double y = Distance * Math.sin(Math.toRadians(AngleY)) * Math.sin(Math.toRadians(AngleX));
        double z = Distance * Math.cos(Math.toRadians(AngleY));

        coordinates[0] = x;
        coordinates[1] = y;
        coordinates[2] = z;

        return coordinates;
    }

    public static double getAngle(double[] p1, double[] p2) {
        // double hAngle = h_angle_pixel_ratio * Math.abs(p1[0]-p2[0]);
        // double vAngle = h_angle_pixel_ratio * Math.abs(p1[1]-p2[1]);
        // double tAngle = Math.pow(Math.pow(hAngle,2) + Math.pow(vAngle,2), 0.5);
        return Math.pow(Math.pow(h_angle_pixel_ratio * Math.abs(p1[0] - p2[0]), 2)
                + Math.pow(h_angle_pixel_ratio * Math.abs(p1[1] - p2[1]), 2), 0.5);
    }

    public static double getAngle(double dx, double dy) {
        // double hAngle = h_angle_pixel_ratio * Math.abs(p1[0]-p2[0]);
        // double vAngle = h_angle_pixel_ratio * Math.abs(p1[1]-p2[1]);
        // double tAngle = Math.pow(Math.pow(hAngle,2) + Math.pow(vAngle,2), 0.5);
        return Math.pow(
                Math.pow(h_angle_pixel_ratio * Math.abs(dx), 2) + Math.pow(h_angle_pixel_ratio * Math.abs(dy), 2), 0.5);
    }

    public static double[] transformCoordsToOffsetAngle(double[] p1) {
        return new double[] { h_angle_pixel_ratio * (p1[0] - img_height / 2),
                v_angle_pixel_ratio * (p1[1] - img_height / 2) };
    }
}
