package frc.robot.commands.intake.vacuum;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;

public class PressureTriggerCommand extends ConditionalCommand {
    public PressureTriggerCommand() {
        super(new TriggerCommandGroup());
    }

    protected boolean condition() {
        return !Robot.myIntake.hasBall;
    }
}


class TriggerCommandGroup extends CommandGroup {
    public TriggerCommandGroup() {
        addParallel(new InstantCommand(this::trigger));
        addParallel(new SetLiftPosition(Lift.Position.CargoPort));
        addParallel(new SetIntakePosition(Intake.Position.Vertical));
    }

    protected void trigger() {
        Robot.myIntake.hasBall = true;
    }
}
