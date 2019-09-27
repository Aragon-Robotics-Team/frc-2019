package frc.robot.commands;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.Command;

public class WaitUntil extends Command {
    Supplier<Boolean> predicate;

    public WaitUntil(Supplier<Boolean> predicate) {
        this.predicate = predicate;
    }

    public boolean isFinished() {
        return predicate.get();
    }
}
