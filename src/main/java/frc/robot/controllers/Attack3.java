package frc.robot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.intake.HoldVacuumOn;
import frc.robot.util.Deadband;

public class Attack3 implements OI {
	Joystick mainJoystick;

	Button slowModeButton;
	Button b4;
	Button b5;
	Button trigger;
	// Button b3;
	// Button b2;
	// Button b10;
	// Button b7;

	static Deadband deadband = new Deadband(0, 0.1);

	// button.whenPressed(new ExampleCommand());

	// button.whileHeld(new ExampleCommand());

	// button.whenReleased(new ExampleCommand());

	public Attack3(int joystickPort) {
		mainJoystick = new Joystick(joystickPort);

		slowModeButton = new JoystickButton(mainJoystick, 2);
		b4 = new JoystickButton(mainJoystick, 4);
		b5 = new JoystickButton(mainJoystick, 5);

		trigger = new JoystickButton(mainJoystick, 1);
		trigger.toggleWhenActive(new HoldVacuumOn());

		// b3 = new JoystickButton(mainJoystick, 3);
		// b2 = new JoystickButton(mainJoystick, 2);

		// b10 = new JoystickButton(mainJoystick, 10);
		// b7 = new JoystickButton(mainJoystick, 7);

		// b4.whenPressed(new SetAngle(-90));
		// b5.whenPressed(new SetAngle(90));
		// b3.whenPressed(new SetAngle(0));
		// b2.whenPressed(new SetAngle(179.99));
		// b10.whenPressed(new ResetAngle());
		// b7.whenPressed(new ControlAngle());

		// b5.whileHeld(new Sol0On());

		// b4.whenPressed(new ControlArcadeDrivetrain());
		// b5.whenPressed(new ControlOneAxisDrivetrain(0.9));
		// b4.whenPressed(new TestNavX());
	}

	public double getLeftSpeed() {
		// System.out.println("left" + mainJoystick.getRawAxis(1));
		return -1.0 * deadband.calc(mainJoystick.getRawAxis(1));
	}

	public double getLeftRotation() {
		// System.out.println("rotate" + mainJoystick.getRawAxis(0));
		return deadband.calc(mainJoystick.getRawAxis(0));
	}

	public double getRightSpeed() {
		return deadband.calc(getLeftSpeed());
	}

	public boolean getSlowMode() {
		return slowModeButton.get();
	}

	public double getAngle() {
		double angle = Math.toDegrees(Math.atan2(getLeftRotation(), getLeftSpeed()));
		// SmartDashboard.putNumber("Angle", angle);
		return angle;
	}
}
