package frc.robot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
import frc.robot.commands.Teleop.*;
import frc.robot.commands.*;

public class F310 implements OI {
	Joystick mainJoystick;

	Button buttonDown;
	Button buttonRight;
	Button buttonLeft;
	Button buttonUp;

	// button.whenPressed(new ExampleCommand());

	// button.whileHeld(new ExampleCommand());

	// button.whenReleased(new ExampleCommand());

	public F310(int joystickPort) {

		mainJoystick = new Joystick(joystickPort);

		buttonDown = new JoystickButton(mainJoystick, 1);
		buttonRight = new JoystickButton(mainJoystick, 2);
		buttonLeft = new JoystickButton(mainJoystick, 3);
		buttonUp = new JoystickButton(mainJoystick, 4);

		buttonDown.whenPressed(new ControlDrivetrain());
		buttonLeft.whenPressed(new ControlOneAxisDrivetrain(0.9));
		buttonRight.whenPressed(new ResetDistance()); // StopDrivetrain
		buttonUp.whenPressed(new ControlArcadeDrivetrain());
	}

	public double getLeftSpeed() {
		System.out.println("left" + mainJoystick.getRawAxis(1));
		return -1.0 * mainJoystick.getRawAxis(1);
	}

	public double getLeftRotation() {
		return mainJoystick.getRawAxis(0);
	}

	public double getRightSpeed() {
		System.out.println("right" + mainJoystick.getRawAxis(5));
		return -1.0 * mainJoystick.getRawAxis(5);
	}

	public boolean getSlowMode() {
		return false;
	}
}