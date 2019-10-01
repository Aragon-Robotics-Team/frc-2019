package frc.robot.util;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class BetterDigitalInput extends Trigger {
    public DigitalInput input;
    public boolean invert;
    public double debounceTime = 0.0;

    double onTime = -1;

    public BetterDigitalInput(int channel) {
        super();

        input = new DigitalInput(channel);
    }

    public BetterDigitalInput(int channel, boolean invert) {
        this(channel);

        this.invert = invert;
    }

    public boolean get() {
        return invert ^ getDebounced();
    }

    protected boolean getDebounced() {
        boolean on = input.get();

        if (on) {
            if (onTime < 0) {
                onTime = Timer.getFPGATimestamp();
            }

            return (Timer.getFPGATimestamp() - onTime) >= debounceTime;
        } else {
            onTime = -1;
            return false;
        }
    }
}
