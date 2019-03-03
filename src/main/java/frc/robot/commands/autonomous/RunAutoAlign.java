package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.teleop.ControlArcadeDrivetrain;

public class RunAutoAlign extends CommandGroup {
    public RunAutoAlign() {
        addSequential(new AutoAlign());
        addSequential(new ControlArcadeDrivetrain());
    }
}
