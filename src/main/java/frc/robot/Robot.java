package frc.robot;

import frc.robot.commands.TestNavX;
import frc.robot.commands.autonomous.AutonomousGroup;
import frc.robot.commands.teleop.TeleopGroup;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.TurnToAngle;
import frc.robot.subsystems.Vision;
import frc.robot.util.BetterRobot;

public class Robot extends BetterRobot {
	public static RobotMap map = RobotMap.getMap();

	// Create subsystem instances here with public static Type var = new Type();
	public static Drivetrain myDrivetrain = new Drivetrain();
	public static NavX myNavX = new NavX();
	public static TurnToAngle myAngle = new TurnToAngle();
	public static Pneumatics myPneumatics = new Pneumatics();
	public static Lift myLift = new Lift();
	public static Intake myIntake = new Intake();
	public static Vision myVision = new Vision();

	// Ran once when Game starts
	@Override
	public void robotInit() {
		System.out.println("init");
	}

	@Override
	public void robotPeriodic() {
	}

	@Override
	public void disabledInit() {
		myAngle.disableAndReset();
		myPneumatics.setCompressor(false);
		myIntake.disable();
	}

	// Ran once when Autonomus stage starts
	@Override
	public void autonomousInit() {
		myPneumatics.setCompressor(true);
		System.out.println("auto");
		AutonomousGroup auto = new AutonomousGroup();
		auto.start();
		Robot.myNavX.zeroYaw();
	}

	// Ran once when Teleop stage starts
	@Override
	public void teleopInit() {
		System.out.println("teleop");
		myPneumatics.setCompressor(true);
		TeleopGroup teleop = new TeleopGroup();
		teleop.start();
		myIntake.resetEncoder();
	}

	@Override
	public void testInit() {
		TestNavX command = new TestNavX();
		command.start();
	}
}
