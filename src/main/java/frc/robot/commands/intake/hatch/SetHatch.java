package frc.robot.commands.intake.hatch;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetHatch extends Command {
    boolean enabled;

    public SetHatch(boolean enabled) {
        setRunWhenDisabled(true);

        this.enabled = enabled;

        System.out.println("Hatch Command make " + enabled);
    }

    protected void initialize() {
        System.out.println("Hatch Command run " + enabled);
        Robot.myIntake.setHatch(enabled);
    }

    protected boolean isFinished() {
        return true;
    }
}
