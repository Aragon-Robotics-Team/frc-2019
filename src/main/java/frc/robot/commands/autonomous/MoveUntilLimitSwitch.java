package frc.robot.commands.autonomous;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;
import edu.wpi.first.wpilibj.command.Command;

public class MoveUntilLimitSwitch extends Command {
    double speed;
    DoubleConsumer setter;
    BooleanSupplier getLimit;
    boolean wantedValue;

    public MoveUntilLimitSwitch(double speed, DoubleConsumer setter, BooleanSupplier getLimit,
            boolean wantedValue) {
        this.speed = speed;
        this.setter = setter;
        this.getLimit = getLimit;
        this.wantedValue = wantedValue;
    }

    protected void initialize() {
    }

    protected void execute() {
        setter.accept(speed);
    }

    protected boolean isFinished() {
        return (getLimit.getAsBoolean() == wantedValue);
    }
}
