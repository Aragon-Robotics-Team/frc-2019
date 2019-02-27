package frc.robot;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class RobotMap {
	public static final int F310_JOYSTICK_0 = 0;
	public static final int ATTACK3_JOYSTICK_0 = 1;
	public static final int ATTACK3_JOYSTICK_1 = 4;

	public static final boolean JOYSTICK_SQUARE_THROTTLE = true;
	public static final boolean JOYSTICK_SQUARE_TURN = true;

	public static final int DRIVETRAIN_LEFT_MAIN_CAN = 2;
	public static final int DRIVETRAIN_RIGHT_MAIN_CAN = 6;
	public static final Class<? extends BaseMotorController> DRIVETRAIN_SLAVE_CLASS =
			TalonSRX.class;
	public static final int DRIVETRAIN_LEFT_SLAVE_CAN = 0;
	public static final int DRIVETRAIN_RIGHT_SLAVE_CAN = 0;

	public static final int PNEUMATICS_CONTROL_MODULE_CAN = 1;

	public static final int LIFT_CAN = 3;

	public static final int INTAKE_CAN = 5;
	public static final int INTAKE_VACUUM_PWM = 0;
	public static final int INTAKE_PISTON_PORT = 0;

	public static final int VISION_LED_RELAY_PORT = 0;

	public static final boolean PNEUMATICS_CONTROL_MODULE_INSTALLED = true;

	public static final boolean DRIVETRAIN_LEFT_MAIN_INSTALLED = true;
	public static final boolean DRIVETRAIN_RIGHT_MAIN_INSTALLED = true;

	public static final boolean LIFT_INSTALLED = true;

	public static final boolean INTAKE_INSTALLED = true;
	public static final boolean INTAKE_VACUUM_INSTALLED = true;
	public static final boolean INTAKE_PISTON_INSTALLED = true;

	public static final boolean NAVX_INSTALLED = true;

	public static final boolean VISION_LED_RELAY_INSTALLED = true;
}
