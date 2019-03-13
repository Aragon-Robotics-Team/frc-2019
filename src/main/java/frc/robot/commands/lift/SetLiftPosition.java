package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Lift.Position;

public class SetLiftPosition extends Command {
    Position pos;

    public SetLiftPosition(Position pos) {
        this.pos = pos;
    }

    protected void initialize() {
        Robot.myLift.setPosition(pos);
    }

    protected boolean isFinished() {
        return true;
    }
}
