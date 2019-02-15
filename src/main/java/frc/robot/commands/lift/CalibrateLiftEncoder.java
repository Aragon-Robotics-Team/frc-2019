package frc.robot.commands.lift;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.autonomous.MoveUntilLimitSwitch;

public class CalibrateLiftEncoder extends CommandGroup {
    static final double speed = 0.5;
    static final DoubleConsumer setSpeed = Robot.myLift.controller::setPercent;
    static final BooleanSupplier getReverseLimit = Robot.myLift.controller::getReverseLimitSwitch;

    public CalibrateLiftEncoder() {
        // Go reverse until hits limit switch, then go forward until release
        addSequential(new MoveUntilLimitSwitch(-speed, setSpeed, getReverseLimit, true));
        addSequential(new MoveUntilLimitSwitch(speed, setSpeed, getReverseLimit, false));
        // Talon SRX should reset encoder automatically when limit switch depressed
    }
}
