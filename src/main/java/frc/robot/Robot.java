package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.*;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.cameraserver.CameraServer;
import frc.robot.commands.Autonomous.*;
import frc.robot.commands.Teleop.*;
import frc.robot.commands.*;
import frc.robot.subsystems.Drivetrain;
import frc.robot.controllers.*;

public class Robot extends TimedRobot {
	public static OI m_oi;

	// Create subsystem instances here with public static Type var = new Type();
	public static Drivetrain myDrivetrain = new Drivetrain();

	// Ran once when Game starts
	@Override
	public void robotInit() {
		m_oi = new SplitAttack3Controller(RobotMap.ATTACK3_JOYSTICK_0, RobotMap.ATTACK3_JOYSTICK_1);
		// m_oi = new Attack3(RobotMap.ATTACK3_JOYSTICK_0);
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
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	// Ran once when Autonomus stage starts
	@Override
	public void autonomousInit() {
		System.out.println("auto");
		myDrivetrain.resetDistance();
		AutonomousGroup auto = new AutonomousGroup();
		auto.start();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	// Ran once when Teleop stage starts
	@Override
	public void teleopInit() {
		System.out.println("teleop");
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
		this.teleopInit();
	}

	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
	}
}
