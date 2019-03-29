package frc.robot.commands.intake.intake;

import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;

public class StowIntakeAlways extends MoveUntilResult<Boolean> {
    boolean stall;

    public StowIntakeAlways(boolean stall) {
        super(-0.3, Robot.myIntake::set, () -> true, false);
        requires(Robot.myIntake.intakeSubsystem);
        this.stall = stall;
    }

    protected void initialize() {
        super.initialize();
        if (stall) {
            Robot.myIntake.controller.talon.overrideLimitSwitchesEnable(false);
        }
    }

    protected void end() {
        super.end();
        if (stall) {
            Robot.myIntake.controller.talon.overrideLimitSwitchesEnable(true);
        }
    }

    protected boolean isFinished() {
        return false;
    }
}
