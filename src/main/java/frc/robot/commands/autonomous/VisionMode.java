package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.ChangeModes;

public class VisionMode extends CommandGroup {
    public VisionMode() {
        addParallel(new ChangeModes());
        addParallel(new AutoAlign(false)); // Requires Drivetrain and Angle
    }
}
