package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.drivetrain.StopDrivetrain;
import frc.robot.commands.intake.vacuum.SetVacuum;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.subsystems.Lift;

public class ChangeModes extends CommandGroup {
    public ChangeModes() {
        addParallel(new StopDrivetrain());
        // addParallel(new SetIntakePosition(Intake.Position.Intake)); // Requires Intake
        addParallel(new SetLiftPosition(Lift.Position.Stowed)); // Requires Lift
        addParallel(new SetVacuum(false)); // Requires Vacuum
    }
}
