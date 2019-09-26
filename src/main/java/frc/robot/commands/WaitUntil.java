package frc.robot.commands;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj.command.Command;

public class WaitUntil extends Command {
    BooleanSupplier predicate;

    public WaitUntil(BooleanSupplier predicate) {
        this.predicate = predicate;
    }

    public boolean isFinished() {
        return predicate.getAsBoolean();
    }
}
