package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class QuickPiston extends CommandGroup {
    static final double timeout = 1.5;

    public QuickPiston() {
        addSequential(new SetPiston(true));
        addSequential(new WaitCommand(timeout));
        addSequential(new SetPiston(false));
    }
}
