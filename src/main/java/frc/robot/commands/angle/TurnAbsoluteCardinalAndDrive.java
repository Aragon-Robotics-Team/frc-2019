package frc.robot.commands.angle;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TurnAbsoluteCardinalAndDrive extends CommandGroup {
    public TurnAbsoluteCardinalAndDrive() {
        addSequential(new TurnAbsoluteCardinal());
        // addSequential(new ControlArcadeDrivetrain());
    }
}
