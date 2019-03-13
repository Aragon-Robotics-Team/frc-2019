package frc.robot.commands.intake.intake;

import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;

public class ClearReverseLimit extends MoveUntilResult<Boolean> {
    public ClearReverseLimit() {
        super(0.05, Robot.myIntake.controller::setOldPercent, Robot.myIntake.controller::getReverseLimitSwitch, false);
    }
}
