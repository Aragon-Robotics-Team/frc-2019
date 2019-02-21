package frc.robot.commands.intake;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.commands.MoveUntilResult;
import frc.robot.subsystems.Intake;

public class CalibrateIntakeEncoder extends CommandGroup {
    static final DoubleConsumer setSpeed = Robot.myIntake.controller::setPercent;
    static final Supplier<Boolean> getReverseLimit =
            Robot.myIntake.controller::getReverseLimitSwitch;

    public CalibrateIntakeEncoder() {
        // Go reverse until hits limit switch, then go forward until release
        addSequential(new MoveUntilResult<Boolean>(-0.75, setSpeed, getReverseLimit, true));
        addSequential(new WaitCommand(0.1));
        addSequential(new MoveUntilResult<Boolean>(0.2, setSpeed, getReverseLimit, false));
        // Talon SRX should reset encoder automatically when limit switch depressed
        addSequential(new SetIntakePosition(Intake.Position.Stowed));
    }
}
