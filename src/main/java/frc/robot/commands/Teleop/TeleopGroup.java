package frc.robot.commands.Teleop;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TeleopGroup extends CommandGroup {

    public TeleopGroup() {
        addSequential(new ControlDrivetrain());
    }
}