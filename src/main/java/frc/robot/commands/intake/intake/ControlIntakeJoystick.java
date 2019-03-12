package frc.robot.commands.intake.intake;

import frc.robot.Robot;
import frc.robot.commands.ControlMotorJoystick;

public class ControlIntakeJoystick extends ControlMotorJoystick {
    public ControlIntakeJoystick() {
        super(Robot.myIntake.controller);
        requires(Robot.myIntake.intakeSubsystem);
    }
}
