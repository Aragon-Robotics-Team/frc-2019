package frc.robot.commands;

import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.command.Command;

// Continue calling setter(velocity) until getter() == wanted
// E.g: move until limit switch hit:

public class MoveUntilResult<T> extends Command {
    double velocity; // Velocity to move at
    DoubleConsumer setter; // Function to call to set velocity
    Supplier<T> getter; // Function to call to get status of limit switch
    T wanted; // Desired return value of getter

    public MoveUntilResult(double vel, DoubleConsumer setter, Supplier<T> getter, T wanted) {
        this.velocity = vel;
        this.setter = setter;
        this.getter = getter;
        this.wanted = wanted;
    }

    protected void execute() {
        if (!_isFinished()) {
            setter.accept(velocity);
        }
    }

    protected void end() {
        setter.accept(0.0);
    }

    // Allow isFinished to be overridden by providing default
    private boolean _isFinished() {
        return (getter.get() == wanted);
    }

    protected boolean isFinished() {
        return _isFinished();
    }
}
