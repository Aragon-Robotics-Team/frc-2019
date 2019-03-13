package frc.robot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public abstract class OIBase extends SendableBase implements OI {
	private Joystick joystick;
	private Button[] buttons;

	private static final int AMOUNT_BUTTONS = 12;

	public OIBase() {
		this(null);
	}

	public OIBase(Integer port) {
		if (port == null) {
			port = getDefaultPort();
		}

		joystick = new Joystick(port);
		buttons = new Button[AMOUNT_BUTTONS + 1]; // So buttons range 1-max; button0 = null

		for (int index = 1; index < (AMOUNT_BUTTONS + 1); index++) {
			buttons[index] = new JoystickButton(joystick, index);
		}

		setUpButtons();
	}

	abstract int getDefaultPort();

	abstract void setUpButtons();

	final Joystick getJoystick() {
		return joystick;
	}

	final Button getButton(int button) {
		return buttons[button];
	}

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
}
