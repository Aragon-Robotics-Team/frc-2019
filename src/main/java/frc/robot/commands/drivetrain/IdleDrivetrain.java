package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.guest.GuestMode;

public class IdleDrivetrain extends CommandGroup {
    public IdleDrivetrain() {
        setRunWhenDisabled(true);
        requires(Robot.myDrivetrain);

        addSequential(new GuestMode());
    }
}
