package frc.robot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;
import frc.robot.util.Deadband;

public class HotasX extends OI {
    Joystick mainJoystick;

    static Deadband deadband = new Deadband(0, 0);

    public double getLeftSpeed() {
        return -deadband.calc(mainJoystick.getRawAxis(2), Robot.map.joystick.squareThrottle());
    }

    public double getLeftRotation() {
        return deadband.calc(mainJoystick.getRawAxis(0), Robot.map.joystick.squareTurn());
    }

    public double getRightSpeed() {
        return 0;
    }
}
