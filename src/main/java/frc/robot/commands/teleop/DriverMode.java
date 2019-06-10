package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.drivetrain.ControlArcadeDrivetrain;
import frc.robot.commands.drivetrain.SetSlowMode;
import frc.robot.subsystems.Drivetrain;

public class DriverMode extends CommandGroup {
    public DriverMode() {
        addParallel(new SetSlowMode(Drivetrain.SlowModes.Normal));
        addParallel(new ControlArcadeDrivetrain());
    }
}
