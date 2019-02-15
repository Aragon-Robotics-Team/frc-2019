package frc.robot.vision;

import java.lang.Math;

public class FindCoordinates {

    public static double[] findCoordinates(double AngleX, double AngleY, double Distance) {

        double coordinates[] = new double[3];
        double x = Distance * Math.sin(Math.toRadians(AngleY)) * Math.cos(Math.toRadians(AngleX));
        double y = Distance * Math.sin(Math.toRadians(AngleY)) * Math.sin(Math.toRadians(AngleX));
        double z = Distance * Math.cos(Math.toRadians(AngleY));

        coordinates[0] = x;
        coordinates[1] = y;
        coordinates[2] = z;

        return coordinates;
    }

}
