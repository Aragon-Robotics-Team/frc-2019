package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.*;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.cameraserver.CameraServer;
import frc.robot.commands.Autonomous.*;
import frc.robot.commands.Teleop.*;
import frc.robot.commands.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.TurnToAngle;
import frc.robot.subsystems.Pneumatics;
import frc.robot.controllers.*;

public class Robot extends TimedRobot {
	public static OI m_oi;

	// Create subsystem instances here with public static Type var = new Type();
	public static Drivetrain myDrivetrain = new Drivetrain();
	public static NavX myNavX = new NavX();
	public static TurnToAngle myAngle = new TurnToAngle();
	public static Pneumatics myPneumatics = new Pneumatics();

	// Ran once when Game starts
	@Override
	public void robotInit() {
		m_oi = new Attack3(RobotMap.ATTACK3_JOYSTICK_0);
		// m_oi = new F310(RobotMap.F310_JOYSTICK_0);

		System.out.println("init");
		// CameraServer.getInstance().startAutomaticCapture();
	}

	@Override
	public void robotPeriodic() {
		// SmartDashboard.putNumber("Joystick Up/Down", m_oi.getLeftSpeed());
		// SmartDashboard.putNumber("L Rate", myDrivetrain.getLeftRate());
		// SmartDashboard.putNumber("R Rate", myDrivetrain.getRightRate());
		// SmartDashboard.putNumber("L Dist", myDrivetrain.getLeftDistance());
		// SmartDashboard.putNumber("R Dist", myDrivetrain.getRightDistance());
	}

	@Override
	public void disabledInit() {
		myAngle.disableAndReset();
		myPneumatics.disableCompressor();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	// Ran once when Autonomus stage starts
	@Override
	public void autonomousInit() {
		myPneumatics.enableCompressor();
		System.out.println("auto");
		myDrivetrain.resetDistance();
		AutonomousGroup auto = new AutonomousGroup();
		auto.start();
		Robot.myNavX.zeroYaw();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	// Ran once when Teleop stage starts
	@Override
	public void teleopInit() {
		System.out.println("teleop");
		myPneumatics.enableCompressor();
		myDrivetrain.resetDistance();
		TeleopGroup teleop = new TeleopGroup();
		teleop.start();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testInit() {
		Shuffleboard.getTab("Drive").add(new SetOpenloopRamp());
		TestNavX command = new TestNavX();
		command.start();
	}

	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
	}
}
