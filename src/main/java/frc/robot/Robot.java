package frc.robot;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.commands.autonomous.AutonomousGroup;
import frc.robot.commands.intake.intake.CalibrateIntakeEncoder;
import frc.robot.commands.teleop.TeleopGroup;
import frc.robot.commands.test.TestGroup;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.TurnToAngle;
import frc.robot.subsystems.Vision;
import frc.robot.util.BetterRobot;
import frc.robot.util.Disableable;
import frc.robot.util.EnableableCommand;

public class Robot extends BetterRobot {
	public static RobotMap map = RobotMap.getMap();

	// Subsytems must come after RobotMaps
	// Create subsystem instances here with public static Type var = new Type();
	public static Drivetrain myDrivetrain = new Drivetrain();
	public static NavX myNavX = new NavX();
	public static TurnToAngle myAngle = new TurnToAngle();
	public static Pneumatics myPneumatics = new Pneumatics();
	public static Lift myLift = new Lift();
	public static Intake myIntake = new Intake();
	public static Vision myVision = new Vision();

	// Commands must come after subsytems
	Command autonomousGroup = new AutonomousGroup(); // Commands will be canceled on disable
	Command teleopGroup = new TeleopGroup();
	Command testGroup = new TestGroup();
	Disableable calibrateIntake = EnableableCommand.of(new CalibrateIntakeEncoder());

	public void _autonomousInit() {
		autonomousGroup.start();
		Robot.myNavX.zeroYaw();
	}

	public void _teleopInit() {
		teleopGroup.start();
	}

	public void _testInit() {
		testGroup.start();
	}
}
