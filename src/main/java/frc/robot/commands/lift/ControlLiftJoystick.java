package frc.robot.commands.lift;

import frc.robot.Robot;
import frc.robot.commands.ControlMotorJoystick;

public class ControlLiftJoystick extends ControlMotorJoystick {
    public ControlLiftJoystick() {
        super(Robot.myLift);
        requires(Robot.myLift);
    }

    protected double getMax() {
        return 0.5;
    }
}
