package frc.robot.commands;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetCompressorEnabled extends Command {
    BooleanSupplier enabled;

    public SetCompressorEnabled(BooleanSupplier enabled) {
        this.enabled = enabled;
    }

    protected void initialize() {
        Robot.myPneumatics.setCompressor(enabled.getAsBoolean());
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }
}
