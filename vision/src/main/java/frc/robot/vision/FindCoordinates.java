package frc.robot.vision;

import java.lang.Math;

public class FindCoordinates {
    public static double radius;

    public static double[] findCoordinates(double AngleX, double AngleY, double Distance) {
        radius = Distance;

        double coordinates[] = new double[3];
        double x = DoWork(AngleX);
        double y = DoWork(AngleY);
        double z = DoWork(90 - AngleX);

        coordinates[0] = x;
        coordinates[1] = y;
        coordinates[2] = z;

        return coordinates;
    }

    public static double DoWork(double Angle) {

        double x = Math.sin(Math.toRadians(Math.abs(Angle))) * radius;

        if (Angle > 0) {
            x = -x;
        }
        return x;
    }
}
