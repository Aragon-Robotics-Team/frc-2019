package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.commands.intake.intake.CalibrateIntakeEncoder;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.lift.CalibrateLiftEncoder;
import frc.robot.subsystems.Intake;

public class Calibrate extends CommandGroup {

    public Calibrate() {
        addParallel(new MoveUntilResult<Boolean>(0.0, Robot.myLift::set, this::intakeReady, true));

        addSequential(new CalibrateIntakeEncoder());
        addSequential(new SetIntakePosition(Intake.Position.WantClearOfLift));
        addSequential(new WaitUntil(this::intakeReady));

        addSequential(new WaitCommand(0.01));
        addSequential(new CalibrateLiftEncoder());
    }

    boolean intakeReady() {
        return Robot.myIntake.getActualPosition() > Intake.Position.ClearOfLift.pos;
    }
}
