package frc.robot.commands.guest;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.drivetrain.SetSlowMode;
import frc.robot.subsystems.Drivetrain;

public class GuestMode extends CommandGroup {
    public GuestMode() {
        addParallel(new SetSlowMode(Drivetrain.SlowModes.Guest));
        addParallel(new GuestDrive()); // Requires Drivetrain
    }
}
