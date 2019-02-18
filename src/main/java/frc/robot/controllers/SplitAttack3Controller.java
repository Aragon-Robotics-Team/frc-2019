package frc.robot.controllers;

public class SplitAttack3Controller extends OI {
    Attack3 leftJoystick;
    Attack3 rightJoystick;

    public SplitAttack3Controller(int leftJoystickPort, int rightJoystickPort) {
        leftJoystick = new Attack3(leftJoystickPort);
        rightJoystick = new Attack3(rightJoystickPort);
    }

    public double getLeftSpeed() {
        return leftJoystick.getLeftSpeed();
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
