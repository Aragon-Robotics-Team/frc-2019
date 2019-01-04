package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.Teleop.StopDrivetrain;

public class AutonomousGroup extends CommandGroup {

    public AutonomousGroup() {
        addSequential(new Autonomous1());
        addSequential(new Autonomous2());
        addSequential(new Autonomous3());
        addSequential(new Autonomous4());
        addSequential(new StopDrivetrain());
    }
}