package frc.robot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
import frc.robot.commands.Debug;
import frc.robot.commands.Teleop.*;

public class Attack3 implements OI {
	Joystick mainJoystick;

	Button slowModeButton;
	Button b4;
	Button b5;

	// button.whenPressed(new ExampleCommand());

	// button.whileHeld(new ExampleCommand());

	// button.whenReleased(new ExampleCommand());

	public Attack3(int joystickPort) {
		mainJoystick = new Joystick(joystickPort);

		slowModeButton = new JoystickButton(mainJoystick, 2);
		b4 = new JoystickButton(mainJoystick, 4);
		b5 = new JoystickButton(mainJoystick, 5);

		// b4.whenPressed(new ControlArcadeDrivetrain());
		// b5.whenPressed(new ControlOneAxisDrivetrain(0.9));

		b5.whenPressed(new Debug());
	}

	public double getLeftSpeed() {
		System.out.println("left" + mainJoystick.getRawAxis(1));
		return -1.0 * mainJoystick.getRawAxis(1);
	}

	public double getLeftRotation() {
		System.out.println("rotate" + mainJoystick.getRawAxis(0));
		return mainJoystick.getRawAxis(0);
	}

	public double getRightSpeed() {
		return getLeftSpeed();
	}

	public boolean getSlowMode() {
		return slowModeButton.get();
	}
}
