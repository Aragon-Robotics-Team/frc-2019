package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.SetCompressorEnabled;

public class TeleopGroup extends CommandGroup {

    public TeleopGroup() {
        addParallel(new SetCompressorEnabled(() -> !Robot.m_oi.getSlowMode()));
        addSequential(new ControlArcadeDrivetrain());
    }
}
