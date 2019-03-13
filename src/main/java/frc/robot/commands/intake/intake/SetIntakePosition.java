package frc.robot.commands.intake.intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import frc.robot.Robot;
import frc.robot.subsystems.Intake.Position;

public class SetIntakePosition extends CommandGroup {
    public SetIntakePosition(Position pos) {
        setRunWhenDisabled(true);

        addSequential(new ConditionalCommand(new ClearReverseLimit()) {
            protected boolean condition() {
                return Robot.myIntake.isStowed();
            }
        });
        addSequential(new RawSetIntakePosition(pos));
    }
}
