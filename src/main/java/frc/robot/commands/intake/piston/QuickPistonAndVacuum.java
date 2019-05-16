package frc.robot.commands.intake.piston;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.commands.intake.vacuum.SetVacuum;

public class QuickPistonAndVacuum extends CommandGroup {
    public QuickPistonAndVacuum() {
        addSequential(new SetPiston(true));
        addSequential(new WaitCommand(1.0));
        addSequential(new SetVacuum(false));
        addSequential(new WaitCommand(0.5));
        addSequential(new SetPiston(false));
    }
}
