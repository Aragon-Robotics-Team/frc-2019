package frc.robot.controllers;

import edu.wpi.first.wpilibj.SendableBase;

public class SplitArcadeAttack3 extends SendableBase implements OI {
    Attack3 leftJoystick;
    Attack3 rightJoystick;

    public SplitArcadeAttack3(int leftJoystickPort, int rightJoystickPort) {
        leftJoystick = new Attack3(leftJoystickPort);
        rightJoystick = new Attack3(rightJoystickPort);
    }

    public double getLeftSpeed() {
        return rightJoystick.getLeftSpeed();
    }

    public double getLeftRotation() {
        return leftJoystick.getLeftRotation();
    }

    public double getRightSpeed() {
        return rightJoystick.getLeftSpeed();
    }

    public boolean getSlowMode() {
        return leftJoystick.getSlowMode() || rightJoystick.getSlowMode();
    }
}
