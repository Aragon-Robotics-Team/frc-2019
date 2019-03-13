package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import frc.robot.Robot;
import frc.robot.subsystems.Lift.Position;

public class SetLiftPosition extends CommandGroup {
    public SetLiftPosition(Position pos) {
        setRunWhenDisabled(true);

        addSequential(new ConditionalCommand(new ClearReverseLimit()) {
            protected boolean condition() {
                return Robot.myLift.isStowed();
            }
        });
        addSequential(new RawSetLiftPosition(pos));
    }
}
