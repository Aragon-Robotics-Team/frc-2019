package frc.robot.util;

import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.DigitalInput;

public class BetterDigitalInput extends Trigger {
    public DigitalInput input;
    public boolean invert;
    public int debouncingSamples = 1;

    int timesOn;

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
            timesOn++;
            return timesOn >= debouncingSamples;
        } else {
            timesOn = 0;
            return false;
        }
    }
}
