package frc.robot.controllers;

import frc.robot.commands.intake.QuickPiston;
import frc.robot.commands.intake.SetIntakePosition;
import frc.robot.commands.intake.SetVacuum;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;
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
		getButton(4).whenPressed(new SetIntakePosition(Intake.Position.Stowed));
		getButton(5).whenPressed(new SetIntakePosition(Intake.Position.Horizontal));
		getButton(3).whenPressed(new SetIntakePosition(Intake.Position.Intake));
		// trigger.whileHeld(new HoldVacuumOn());

		getButton(6).whenPressed(new QuickPiston());
		getButton(7).whenPressed(new SetLiftPosition(Lift.Position.Hatch1));
		getButton(10).whenPressed(new SetLiftPosition(Lift.Position.Hatch2));
		getButton(11).whenPressed(new SetLiftPosition(Lift.Position.Port1));

		getButton(8).whenPressed(new SetVacuum(true));
		getButton(9).whenPressed(new SetVacuum(false));
	}

	public double getLeftSpeed() {
		return -deadband.calc(getJoystick().getRawAxis(1), RobotMap.Joystick.squareThrottle());
	}

	public double getLeftRotation() {
		return deadband.calc(getJoystick().getRawAxis(0), RobotMap.Joystick.squareTurn());
	}

	public double getRightSpeed() {
		return deadband.calc(getLeftSpeed());
	}

	public boolean getSlowMode() {
		return getButton(2).get();
	}
}
