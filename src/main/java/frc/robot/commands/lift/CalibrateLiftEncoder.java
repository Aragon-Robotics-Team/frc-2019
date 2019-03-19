package frc.robot.commands.lift;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;
import frc.robot.subsystems.Lift.Position;

public class CalibrateLiftEncoder extends CommandGroup {
    static final DoubleConsumer setSpeed = Robot.myLift::set;
    static final Supplier<Boolean> getReverseLimit = Robot.myLift.controller::getReverseLimitSwitch;

    public CalibrateLiftEncoder() {
        requires(Robot.myLift);

        addSequential(new MoveUntilResult<Boolean>(-0.75, setSpeed, getReverseLimit, true));
        addSequential(new LiftClearReverseLimit());
        addSequential(new SetLiftPosition(Position.Stowed));
        // idk if the following is the best idea
        addSequential(new WaitCommand(0.5));
        addSequential(new ResetLiftEncoder());
    }
}
