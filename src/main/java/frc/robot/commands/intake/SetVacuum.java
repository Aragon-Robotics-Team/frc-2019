package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetVacuum extends Command {
    boolean enabled;

    public SetVacuum(boolean enabled) {
        this.enabled = enabled;
    }

    protected void initialize() {
        Robot.myIntake.setVacuum(enabled);
    }

    protected boolean isFinished() {
        return true;
    }
}
