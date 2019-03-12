package frc.robot.commands.intake.vacuum;

import frc.robot.Robot;
import frc.robot.commands.ControlMotorJoystick;
import frc.robot.util.BetterSpeedController;

public class ControlVacuumJoystick extends ControlMotorJoystick {
    public ControlVacuumJoystick() {
        super(BetterSpeedController.wrap(Robot.myIntake.vacuumController));
        requires(Robot.myIntake.vacuumSubsystem);
    }
}
