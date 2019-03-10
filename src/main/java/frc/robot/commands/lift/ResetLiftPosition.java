package frc.robot.commands.lift;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Lift.Position;

public class ResetLiftPosition extends Command {
    public ResetLiftPosition() {
        setRunWhenDisabled(true);
    }

    protected void init() {
        Robot.myLift.setPosition(Position.Stowed);
    }

    protected boolean isFinished() {
        return true;
    }
}
