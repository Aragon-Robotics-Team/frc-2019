package frc.robot.commands.intake;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;

public class CalibrateIntakeEncoder extends CommandGroup {
    static final DoubleConsumer setSpeed = Robot.myIntake.controller::setOldPercent;
    static final Supplier<Boolean> getReverseLimit =
            Robot.myIntake.controller::getReverseLimitSwitch;

    public CalibrateIntakeEncoder() {

    }
}
