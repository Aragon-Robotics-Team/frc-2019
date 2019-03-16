package frc.robot.commands.intake.intake;

import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;

public class StowLiftAlways extends MoveUntilResult<Boolean> {
    public StowLiftAlways() {
        super(-0.5, Robot.myIntake::set, () -> true, false);
        requires(Robot.myIntake.intakeSubsystem);
    }

    protected boolean isFinished() {
        return false;
    }
}
