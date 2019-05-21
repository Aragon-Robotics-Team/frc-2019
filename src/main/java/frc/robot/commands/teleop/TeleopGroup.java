package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class TeleopGroup extends CommandGroup {

    public TeleopGroup() {
        // addParallel(new SetCompressorEnabled(() -> !Robot.map.oi.getSlowMode()));
        // addParallel(new ResetLiftEncoder());
        // addParallel(new ResetIntakeEncoder());
        addSequential(new WaitCommand(1.0));
        addParallel(new DriverMode());
        // addParallel(new AutoAlign());
    }
}
