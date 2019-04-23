package frc.robot.controllers;

import edu.wpi.first.wpilibj.SendableBase;

public class NullOI extends SendableBase implements OI {
    public double getLeftSpeed() {
        return 0.0;
    }

    public double getLeftRotation() {
        return 0.0;
    }

    public double getRightSpeed() {
        return 0.0;
    }

    public boolean getSlowMode() {
        return false;
    }
}
