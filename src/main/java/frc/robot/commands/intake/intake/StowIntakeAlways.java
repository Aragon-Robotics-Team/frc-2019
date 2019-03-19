package frc.robot.commands.intake.intake;

import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;

public class StowIntakeAlways extends MoveUntilResult<Boolean> {
    public StowIntakeAlways() {
        super(-0.5, Robot.myIntake::set, () -> true, false);
        requires(Robot.myIntake.intakeSubsystem);
    }

    protected boolean isFinished() {
        return false;
    }
}
