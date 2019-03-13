package frc.robot.util;

import edu.wpi.first.wpilibj.SpeedController;

public interface BetterSpeedController {
    public static BetterSpeedController wrap(SpeedController controller) {
        return new BetterSpeedController() {
            public void set(double speed) {
                controller.set(speed);
            }

            public double get() {
                return controller.get();
            }

            public String toString() {
                return controller.toString();
            }
        };
    }

    default void set(double speed) {
    }

    default double get() {
        return 0;
    }
}
