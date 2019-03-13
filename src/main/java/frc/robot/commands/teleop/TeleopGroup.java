package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.SetCompressorEnabled;
import frc.robot.commands.drivetrain.ControlArcadeDrivetrain;
import frc.robot.commands.lift.ResetLiftEncoder;

public class TeleopGroup extends CommandGroup {

    public TeleopGroup() {
        addParallel(new SetCompressorEnabled(() -> !Robot.map.oi.getSlowMode()));
        addParallel(new ResetLiftEncoder());
        addParallel(new ControlArcadeDrivetrain());
    }
}
