package frc.robot.util;

import edu.wpi.first.wpilibj.SpeedController;

public interface BetterSpeedController {
    public static BetterSpeedController wrap(SpeedController controller) {
        // if (controller.getClass() == BetterSpeedController.class) {
        //     // It's already a BetterSpeedController
        //     return (BetterSpeedController) controller;
        // } else {
            // Create wrapper class
            return new BetterSpeedController() {
                public void set(double speed) {
                    controller.set(speed);
                }

                public double get() {
                    return controller.get();
                }
            };
        // }
    }

    default void set(double speed) {
    }

    default double get() {
        return 0;
    }
}
