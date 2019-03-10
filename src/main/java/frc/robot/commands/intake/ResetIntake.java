package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.Intake.Position;

public class ResetIntake extends CommandGroup {
    public ResetIntake() {
        setRunWhenDisabled(true);

        addParallel(new SetIntakePosition(Position.Stowed));
        addParallel(new InstantCommand(() -> Robot.myIntake.setVacuum(false)));
        addParallel(new SetPiston(false));
    }
}
