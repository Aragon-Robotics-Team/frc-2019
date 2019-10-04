package frc.robot.commands.intake.intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.intake.hatch.SetHatch;
import frc.robot.subsystems.Intake.Position;

public class SetIntakePosition extends CommandGroup {
    public SetIntakePosition(Position pos) {
        setRunWhenDisabled(true);

        if (pos == Position.Stowed) {
            addParallel(new SetHatch(false));
        }

        if (pos == Position.Intake) {
            addParallel(new SetHatch(true));
        }
        
        if (pos == Position.Horizontal) {
            addParallel(new SetHatch(false));
        }

        addSequential(new IntakeClearReverseLimit());
        addSequential(new RawSetIntakePosition(pos));
    }
}
