package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.teleop.TeleopGroup;

public class AutonomousGroup extends CommandGroup {
    public AutonomousGroup() {
        addSequential(new TeleopGroup());
    }
}
