package frc.robot.util;

public interface Disableable {
    void disable();

    default void enable() {
    }
}
