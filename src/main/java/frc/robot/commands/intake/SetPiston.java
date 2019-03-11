package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetPiston extends Command {
    boolean enabled;

    public SetPiston(boolean enabled) {
        setRunWhenDisabled(true);

        this.enabled = enabled;
    }

    protected void initialize() {
        Robot.myIntake.setPiston(enabled);
    }

    protected boolean isFinished() {
        return true;
    }
}
