package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class IdleDrivetrain extends CommandGroup {
    public IdleDrivetrain() {
        setRunWhenDisabled(true);

        addSequential(new StopDrivetrain());
    }
}
