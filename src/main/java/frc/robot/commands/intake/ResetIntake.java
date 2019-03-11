package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.intake.piston.SetPiston;
import frc.robot.commands.intake.vacuum.SetVacuum;
import frc.robot.subsystems.Intake.Position;

public class ResetIntake extends CommandGroup {
    public ResetIntake() {
        setRunWhenDisabled(true);

        addParallel(new SetIntakePosition(Position.Stowed));
        addParallel(new SetVacuum(false));
        addParallel(new SetPiston(false));
    }
}
