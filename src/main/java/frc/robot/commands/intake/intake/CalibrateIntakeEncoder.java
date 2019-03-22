package frc.robot.commands.intake.intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;

public class CalibrateIntakeEncoder extends CommandGroup {
    public CalibrateIntakeEncoder() {
        requires(Robot.myIntake.intakeSubsystem);

        addSequential(new ResetIntakeEncoder());
    }
}
