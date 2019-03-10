package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ResetDrivetrain extends CommandGroup {
    public ResetDrivetrain() {
        setRunWhenDisabled(true);

        addParallel(new StopDrivetrain());
        addParallel(new SetBrakeMode(true));
        addParallel(new ResetDrivetrainLocator());
    }
}
