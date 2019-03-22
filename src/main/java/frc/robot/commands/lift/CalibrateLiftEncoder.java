package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;

public class CalibrateLiftEncoder extends CommandGroup {
    public CalibrateLiftEncoder() {
        requires(Robot.myLift);
    }
}
