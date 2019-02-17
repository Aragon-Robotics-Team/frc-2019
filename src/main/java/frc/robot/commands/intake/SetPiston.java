package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Intake;

public class SetPiston extends Command {
    boolean enabled;

    public SetPiston(boolean enabled) {
        this.enabled = enabled;
    }

    protected void initialize() {
        Robot.myIntake.setPiston(enabled);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }
}
