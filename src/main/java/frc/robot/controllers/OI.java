package frc.robot.controllers;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.util.BetterSendable;
import frc.robot.util.SendableMaster;

public interface OI extends BetterSendable, Sendable {
    public double getLeftSpeed();

    public double getLeftRotation();

    public double getRightSpeed();

    public boolean getSlowMode();

    public default boolean disableCompressor() {
        return getSlowMode();
    }

    public default double getAngle() {
        return Math.toDegrees(Math.atan2(getLeftRotation(), getLeftSpeed()));
    }

    public default String getTabName() {
        return "Drivetrain";
    }

    public default void createSendable(SendableMaster master) {
        master.add((Sendable) this);
    }

    public default void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", this::getAngle, null);
        builder.addDoubleProperty("Y", this::getLeftSpeed, null);
        builder.addDoubleProperty("X", this::getLeftRotation, null);
        builder.addBooleanProperty("SloMo", this::getSlowMode, null);
        builder.addBooleanProperty("Disable Compressor", this::disableCompressor, null);
    }
}
