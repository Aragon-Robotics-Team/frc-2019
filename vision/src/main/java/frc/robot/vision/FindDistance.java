package frc.robot.vision;

import frc.robot.vision.GripPostProcessing;

public class FindDistance {
    public void findDistance(double d, double w, double p) {
        double f; /// focal length
        double d2; // distance from camera

        f = (p * d) / w;

        d2 = (w * f) / p;


    }
}
