package frc.robot.commands.intake;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;
import frc.robot.subsystems.Intake;

public class CalibrateIntakeEncoder extends CommandGroup {
    static final double speed = 0.4;
    static final DoubleConsumer setSpeed = Robot.myIntake.controller::setPercent;
    static final Supplier<Boolean> getReverseLimit =
            Robot.myIntake.controller::getReverseLimitSwitch;

    public CalibrateIntakeEncoder() {
        // Go reverse until hits limit switch, then go forward until release
        addSequential(new MoveUntilResult<Boolean>(-speed, setSpeed, getReverseLimit, true));
        addSequential(new MoveUntilResult<Boolean>(speed, setSpeed, getReverseLimit, false));
        // Talon SRX should reset encoder automatically when limit switch depressed
        addSequential(new SetIntakePosition(Intake.Position.Stowed));
    }
}
