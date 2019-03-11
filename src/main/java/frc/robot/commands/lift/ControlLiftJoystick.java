package frc.robot.commands.lift;

import frc.robot.Robot;
import frc.robot.commands.ControlMotorJoystick;

public class ControlLiftJoystick extends ControlMotorJoystick {
    public ControlLiftJoystick() {
        super(Robot.myLift.controller);
        requires(Robot.myLift);
    }
}
