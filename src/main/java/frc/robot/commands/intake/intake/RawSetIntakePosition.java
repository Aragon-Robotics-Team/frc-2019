package frc.robot.commands.intake.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Intake.Position;

public class RawSetIntakePosition extends Command {
    Position pos;

    public RawSetIntakePosition(Position pos) {
        // requires(Robot.myIntake.intakeSubsystem);
        setRunWhenDisabled(true);

        this.pos = pos;
    }

    protected void initialize() {
        Robot.myIntake.setPosition(pos);
    }

    protected boolean isFinished() {
        return true;
    }
}
