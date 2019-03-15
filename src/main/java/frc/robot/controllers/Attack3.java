package frc.robot.controllers;

import frc.robot.commands.intake.piston.QuickPiston;
import frc.robot.commands.intake.vacuum.SetVacuum;
import frc.robot.map.RobotMap;
import frc.robot.util.Deadband;

public class Attack3 extends OIBase {
	static Deadband deadband = new Deadband(0, 0.15);

	public Attack3() {
		super();
	}

	public Attack3(Integer port) {
		super(port);
	}

	int getDefaultPort() {
		return RobotMap.Joystick.attack3_0Port();
	}

	void setUpButtons() {
		getButton(1).whenPressed(new QuickPiston());

		getButton(3).whenPressed(new SetVacuum(true));
		getButton(2).whenPressed(new SetVacuum(false));
	}

	public double getLeftSpeed() {
		return -deadband.calc(getJoystick().getRawAxis(1), RobotMap.Joystick.squareThrottle());
	}

	public double getLeftRotation() {
		return deadband.calc(getJoystick().getRawAxis(0), RobotMap.Joystick.squareTurn());
	}

	public double getRightSpeed() {
		return -deadband.calc(getJoystick().getRawAxis(2)); // Mini slider at bottom
	}

	public boolean getSlowMode() {
		return getButton(2).get();
	}
}
