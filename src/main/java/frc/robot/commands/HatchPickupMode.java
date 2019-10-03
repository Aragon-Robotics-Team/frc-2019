package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;

public class HatchPickupMode extends CommandGroup {
    public HatchPickupMode() {
        addParallel(new SetLiftPosition(Lift.Position.Port1));
        addParallel(new SetIntakePosition(Intake.Position.Horizontal));
    }
}
