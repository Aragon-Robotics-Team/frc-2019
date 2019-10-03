package frc.robot.commands.intake.hatch;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.subsystems.Intake;

public class HatchPosition extends CommandGroup {
    public HatchPosition() {
        addParallel(new SetHatch(false));
        addParallel(new SetIntakePosition(Intake.Position.Horizontal));
    }
}
