package frc.robot.commands.guest;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class GuestDrive extends Command {
    public GuestDrive() {
        requires(Robot.myDrivetrain);
    }

    protected void execute() {
        Robot.myDrivetrain.controlArcade(Robot.map.guestOI.getLeftSpeed(),
                Robot.map.guestOI.getLeftRotation());
    }

    protected boolean isFinished() {
        return false;
    }
}
