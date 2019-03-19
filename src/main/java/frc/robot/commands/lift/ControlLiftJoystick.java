package frc.robot.commands.lift;

import frc.robot.Robot;
import frc.robot.commands.ControlMotorJoystick;
import frc.robot.subsystems.Lift.Position;

public class ControlLiftJoystick extends ControlMotorJoystick {
    boolean isZero;

    public ControlLiftJoystick() {
        super(Robot.myLift);
        requires(Robot.myLift);
        isZero = false;
    }

    protected void execute() {
        if (getValue() == 0) {
            if (!isZero) {
                Robot.myLift.setCustomSetpoint(Robot.myLift.controller.getEncoderPos());
            }
            isZero = true;
            Robot.myLift.setPosition(Position.Manual);
        } else {
            isZero = false;
            super.execute();
        }
    }

    protected double getMax() {
        return 0.5;
    }
}
