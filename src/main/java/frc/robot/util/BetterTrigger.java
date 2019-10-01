
package frc.robot.util;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class BetterTrigger extends Trigger {
    public Trigger[] triggers;

    public BetterTrigger(Trigger... triggers) {
        this.triggers = triggers;
    }

    public BetterTrigger(Supplier<Boolean> supplier) {
        triggers = new Trigger[] {new Trigger() {
            public boolean get() {
                return supplier.get();
            }
        }};
    }

    public boolean get() {
        boolean val = true;

        for (Trigger trigger : triggers) {
            val &= trigger.get();
        }

        return val;
    }
}
