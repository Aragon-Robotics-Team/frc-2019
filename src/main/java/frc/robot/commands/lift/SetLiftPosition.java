package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Lift;

public class SetLiftPosition extends Command {
    Lift.Position pos;

    public SetLiftPosition(Lift.Position pos) {
        this.pos = pos;
    }

    protected void initialize() {
        Robot.myLift.setPosition(pos);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }
}
