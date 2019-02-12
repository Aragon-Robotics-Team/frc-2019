package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.commands.SetOpenloopRamp;
import frc.robot.commands.TestNavX;
import frc.robot.commands.autonomous.AutonomousGroup;
import frc.robot.commands.teleop.TeleopGroup;
import frc.robot.controllers.Attack3;
import frc.robot.controllers.OI;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.TurnToAngle;

public class Robot extends TimedRobot {
	public static OI m_oi;

	// Create subsystem instances here with public static Type var = new Type();
	public static Drivetrain myDrivetrain = new Drivetrain();
	public static NavX myNavX = new NavX();
	public static TurnToAngle myAngle = new TurnToAngle();
	public static Pneumatics myPneumatics = new Pneumatics();
	public static Lift myLift = new Lift();

	// Ran once when Game starts
	@Override
	public void robotInit() {
		m_oi = new Attack3(RobotMap.ATTACK3_JOYSTICK_0);
		// m_oi=new SplitAttack3Controller(RobotMap.ATTACK3_JOYSTICK_0,RobotMap.ATTACK3_JOYSTICK_1);
		// m_oi = new F310(RobotMap.F310_JOYSTICK_0);

		System.out.println("init");
	}

	@Override
	public void robotPeriodic() {
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
