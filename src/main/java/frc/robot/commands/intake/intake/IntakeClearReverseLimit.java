package frc.robot.commands.intake.intake;

import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;

public class IntakeClearReverseLimit extends MoveUntilResult<Boolean> {
    public IntakeClearReverseLimit() {
        super(0.05, Robot.myIntake::set, Robot.myIntake.controller::getReverseLimitSwitch, false);
    }
}
