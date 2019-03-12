package frc.robot.commands.intake.intake;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;
import frc.robot.subsystems.Intake.Position;

public class CalibrateIntakeEncoder extends CommandGroup {
    static final DoubleConsumer setSpeed = Robot.myIntake.controller::setOldPercent;
    static final Supplier<Boolean> getReverseLimit =
            Robot.myIntake.controller::getReverseLimitSwitch;

    public CalibrateIntakeEncoder() {
        requires(Robot.myIntake.intakeSubsystem);

        // Go reverse until hits limit switch, then go forward until release
        addSequential(new MoveUntilResult<Boolean>(-0.5, setSpeed, getReverseLimit, true));
        // addSequential(new WaitCommand(0.25));
        addSequential(new MoveUntilResult<Boolean>(0.1, setSpeed, getReverseLimit, false));
        // Talon SRX should reset encoder automatically when limit switch depressed
        addSequential(new SetIntakePosition(Position.Stowed));
    }
}
