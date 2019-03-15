package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.SetCompressorEnabled;
import frc.robot.commands.angle.SetAngle;
import frc.robot.commands.drivetrain.ControlArcadeDrivetrain;
import frc.robot.commands.lift.ResetLiftEncoder;

public class TurnAndThenDrive extends CommandGroup {

    public TurnAndThenDrive(boolean direction) {
        if (direction == true) {
            addSequential(new SetAngle(Robot.myAngle.currentAngle + 90));
        } else {
            addSequential(new SetAngle(Robot.myAngle.currentAngle - 90));
        }
        addParallel(new SetCompressorEnabled(() -> !Robot.map.oi.getSlowMode()));
        addParallel(new ResetLiftEncoder());
        addParallel(new ControlArcadeDrivetrain());
    }
}
