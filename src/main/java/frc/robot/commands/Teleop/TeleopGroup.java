package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.lift.ControlLiftJoystick;

public class TeleopGroup extends CommandGroup {

    public TeleopGroup() {
        addSequential(new ControlLiftJoystick());
    }
}
