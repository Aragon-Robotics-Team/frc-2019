package frc.robot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.Robot;
import frc.robot.commands.intake.HoldVacuumOn;
import frc.robot.commands.intake.QuickPiston;
import frc.robot.commands.intake.SetIntakePosition;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;
import frc.robot.util.Deadband;

public class Attack3 extends OI {
	Joystick mainJoystick;

	Button slowModeButton;
	Button b4;
	Button b5;
	Button trigger;
	Button b3;
	Button b6;
	Button b7;
	Button b10;
	Button b11;
	Button b8;
	Button b9;
	// Button b3;
	// Button b2;
	// Button b10;
	// Button b7;

	static Deadband deadband = new Deadband(0, 0.15);

	// button.whenPressed(new ExampleCommand());

	// button.whileHeld(new ExampleCommand());

	// button.whenReleased(new ExampleCommand());

	public Attack3(int joystickPort) {
		mainJoystick = new Joystick(joystickPort);

		slowModeButton = new JoystickButton(mainJoystick, 2);
		b4 = new JoystickButton(mainJoystick, 4);
		b5 = new JoystickButton(mainJoystick, 5);
		b3 = new JoystickButton(mainJoystick, 3);
		trigger = new JoystickButton(mainJoystick, 1);

		b6 = new JoystickButton(mainJoystick, 6);
		b7 = new JoystickButton(mainJoystick, 7);
		b10 = new JoystickButton(mainJoystick, 10);
		b11 = new JoystickButton(mainJoystick, 11);

		b8 = new JoystickButton(mainJoystick, 8);
		b9 = new JoystickButton(mainJoystick, 9);

		b4.whenPressed(new SetIntakePosition(Intake.Position.Stowed));
		b5.whenPressed(new SetIntakePosition(Intake.Position.Horizontal));
		b3.whenPressed(new SetIntakePosition(Intake.Position.Intake));
		// trigger.whileHeld(new HoldVacuumOn());

		b6.whenPressed(new QuickPiston());
		b7.whenPressed(new SetLiftPosition(Lift.Position.HATCH_1));
		b10.whenPressed(new SetLiftPosition(Lift.Position.HATCH_2));
		b11.whenPressed(new SetLiftPosition(Lift.Position.PORT_1));

		HoldVacuumOn vacuum = new HoldVacuumOn();

		b8.whenPressed(vacuum);
		b9.cancelWhenPressed(vacuum);

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
		return -deadband.calc(mainJoystick.getRawAxis(1), Robot.map.joystick.squareThrottle());
	}

	public double getLeftRotation() {
		// System.out.println("rotate" + mainJoystick.getRawAxis(0));
		return deadband.calc(mainJoystick.getRawAxis(0), Robot.map.joystick.squareTurn());
	}

	public double getRightSpeed() {
		return deadband.calc(getLeftSpeed());
	}

	public boolean getSlowMode() {
		return slowModeButton.get();
	}

}
