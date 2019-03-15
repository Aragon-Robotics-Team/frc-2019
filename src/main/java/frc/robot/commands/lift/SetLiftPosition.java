package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.subsystems.Lift.Position;

public class SetLiftPosition extends CommandGroup {
    public SetLiftPosition(Position pos) {
        setRunWhenDisabled(true);

        addSequential(new LiftClearReverseLimit());
        addSequential(new RawSetLiftPosition(pos));
    }
}
