package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ResetIntakeEncoder extends Command {
    public ResetIntakeEncoder() {
        setRunWhenDisabled(true);
    }

    protected void initialize() {
        Robot.myIntake.resetEncoder();
    }

    protected boolean isFinished() {
        return true;
    }
}
