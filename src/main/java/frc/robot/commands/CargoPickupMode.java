package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.intake.vacuum.SetVacuum;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;

public class CargoPickupMode extends CommandGroup {
    public CargoPickupMode() {
        addParallel(new SetLiftPosition(Lift.Position.Stowed));
        addParallel(new SetIntakePosition(Intake.Position.Intake));
        addParallel(new SetVacuum(true));
    }
}
