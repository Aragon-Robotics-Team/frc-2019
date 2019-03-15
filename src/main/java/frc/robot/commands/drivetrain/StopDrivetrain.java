package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class StopDrivetrain extends Command {

    public StopDrivetrain() {
        setRunWhenDisabled(true);
        // requires(Robot.myDrivetrain);
    }

    protected void initialize() {
        Robot.myDrivetrain.stop();
    }

    protected boolean isFinished() {
        return true;
    }
}
