package frc.robot.commands.intake.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Intake.Position;

public class SetIntakePosition extends Command {
    Position pos;

    public SetIntakePosition(Position pos) {
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
