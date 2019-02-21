package frc.robot.controllers;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public abstract class OI extends SendableBase {
	public double getLeftSpeed() {
		return 0;
	}

	public double getLeftRotation() {
		return 0;
	}

	public double getRightSpeed() {
		return 0;
	}

	public boolean getSlowMode() {
		return false;
	}

	public double getAngle() {
		return Math.toDegrees(Math.atan2(getLeftRotation(), getLeftSpeed()));
	}

	public void addShuffleboard() {
		Shuffleboard.getTab("Drive").add("Joystick", this);
	}

	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("Gyro");
		builder.addDoubleProperty("Value", this::getAngle, null);
		builder.addDoubleProperty("Y", this::getLeftSpeed, null);
		builder.addDoubleProperty("X", this::getLeftRotation, null);
		builder.addBooleanProperty("SloMo", this::getSlowMode, null);
	}
}
