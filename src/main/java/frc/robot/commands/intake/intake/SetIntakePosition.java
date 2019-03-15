package frc.robot.commands.intake.intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.subsystems.Intake.Position;

public class SetIntakePosition extends CommandGroup {
    public SetIntakePosition(Position pos) {
        setRunWhenDisabled(true);

        addSequential(new IntakeClearReverseLimit());
        addSequential(new RawSetIntakePosition(pos));
    }
}
