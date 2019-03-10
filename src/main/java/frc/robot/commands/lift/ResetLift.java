package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ResetLift extends CommandGroup {
    public ResetLift() {
        setRunWhenDisabled(true);

        addParallel(new ResetLiftEncoder());
        addParallel(new ResetLiftPosition());
    }
}
