package frc.robot.controllers;

import edu.wpi.first.wpilibj.SendableBase;

// Create multiple OIs where only the first has an impact on the joystick axis
// e.g. ButtonBoard
public class MultiOI extends SendableBase implements OI {
    OI master;
    OI[] slaves;

    public MultiOI(OI master, OI... slaves) {
        this.master = master;
        this.slaves = slaves;
    }

    public double getLeftSpeed() {
        return master.getLeftSpeed();
    }

    public double getLeftRotation() {
        return master.getLeftRotation();
    }

    public double getRightSpeed() {
        return master.getRightSpeed();
    }

    public boolean getSlowMode() {
        return master.getSlowMode();
    }

    public boolean disableCompressor() {
        return master.disableCompressor();
    }
}
