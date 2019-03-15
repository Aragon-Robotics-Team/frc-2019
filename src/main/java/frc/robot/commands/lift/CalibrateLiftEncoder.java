package frc.robot.commands.lift;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;
import frc.robot.subsystems.Lift.Position;

public class CalibrateLiftEncoder extends CommandGroup {
    static final DoubleConsumer setSpeed = Robot.myLift.controller::setOldPercent;
    static final Supplier<Boolean> getReverseLimit = Robot.myLift.controller::getReverseLimitSwitch;

    public CalibrateLiftEncoder() {
        requires(Robot.myLift);

        addSequential(new MoveUntilResult<Boolean>(-0.75, setSpeed, getReverseLimit, true));
        addSequential(new LiftClearReverseLimit());
        addSequential(new SetLiftPosition(Position.Stowed));
    }
}
