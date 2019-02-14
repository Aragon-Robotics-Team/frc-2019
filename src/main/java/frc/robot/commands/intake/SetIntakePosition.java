package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Intake;

public class SetIntakePosition extends Command {
    Intake.Position pos;

    public SetIntakePosition(Intake.Position pos) {
        this.pos = pos;
    }

    protected void initialize() {
        Robot.myIntake.setPos(pos);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }
}
