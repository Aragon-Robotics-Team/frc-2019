package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.SetCompressorEnabled;
import frc.robot.commands.autonomous.AutoAlign;

public class TeleopGroup extends CommandGroup {

    public TeleopGroup() {
        addParallel(new SetCompressorEnabled(() -> !Robot.map.oi.getSlowMode()));
        // addParallel(new ResetLiftEncoder());
        // addParallel(new ResetIntakeEncoder());
        // addParallel(new ControlArcadeDrivetrain());
        addParallel(new AutoAlign());
    }
}
