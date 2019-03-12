package frc.robot.util;

import edu.wpi.first.wpilibj.command.Command;

public class EnableableCommand {
    private EnableableCommand() {
    }

    public static Disableable of(Command command) {
        return new Disableable() {
            public void disable() {
                command.cancel();
            }

            public void enable() {
                command.start();
            }
        };
    }
}
