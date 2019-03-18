package frc.robot.commands.lift;

import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;

public class LiftClearReverseLimit extends MoveUntilResult<Boolean> {
    public LiftClearReverseLimit() {
        super(0.1, Robot.myLift::set, Robot.myLift.controller::getReverseLimitSwitch, false);
    }
}
