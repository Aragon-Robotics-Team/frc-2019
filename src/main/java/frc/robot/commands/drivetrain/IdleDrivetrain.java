package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;

public class IdleDrivetrain extends CommandGroup {
    public IdleDrivetrain() {
        setRunWhenDisabled(true);
        requires(Robot.myDrivetrain);

        // addSequential(new StopDrivetrain());
        addSequential(new ControlArcadeDrivetrain());
    }
}
