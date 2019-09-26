package frc.robot.commands;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.intake.intake.CalibrateIntakeEncoder;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.lift.CalibrateLiftEncoder;
import frc.robot.subsystems.Intake;

public class Calibrate extends CommandGroup {

    public Calibrate() {
        addSequential(new CalibrateIntakeEncoder());
        addSequential(new SetIntakePosition(Intake.Position.WantClearOfLift));
        addSequential(new WaitUntil(new BooleanSupplier() {
            public boolean getAsBoolean() {
                return Robot.myIntake.getActualPosition() > Intake.Position.ClearOfLift.pos;
            }
        }));

        addSequential(new CalibrateLiftEncoder());
    }
}
