package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Lift.Position;

public class RawSetLiftPosition extends Command {
    Position pos;

    public RawSetLiftPosition(Position pos) {
        requires(Robot.myLift);
        setRunWhenDisabled(true);

        this.pos = pos;
    }

    protected void initialize() {
        Robot.myLift.setPosition(pos);
    }

    protected boolean isFinished() {
        return true;
    }
}
