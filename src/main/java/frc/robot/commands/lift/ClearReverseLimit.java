package frc.robot.commands.lift;

import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;

public class ClearReverseLimit extends MoveUntilResult<Boolean> {
    public ClearReverseLimit() {
        super(0.25, Robot.myLift.controller::setOldPercent,
                Robot.myLift.controller::getReverseLimitSwitch, false);
    }
}
