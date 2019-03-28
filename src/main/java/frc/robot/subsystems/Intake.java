package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.commands.intake.intake.CalibrateIntakeEncoder;
import frc.robot.commands.intake.intake.ControlIntakeJoystick;
import frc.robot.commands.intake.intake.ResetIntakeEncoder;
import frc.robot.commands.intake.vacuum.ControlVacuumJoystick;
import frc.robot.commands.intake.vacuum.SetVacuum;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSolenoid;
import frc.robot.util.BetterSpeedController;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.Disableable;
import frc.robot.util.Mock;
import frc.robot.util.SendableMaster;

public class Intake extends BetterSubsystem
        implements BetterSendable, Disableable, BetterSpeedController {
    public BetterTalonSRX controller;
    public Talon vacuumController;
    BetterSolenoid pistonController;
    public BetterSubsystem intakeSubsystem;
    public BetterSubsystem vacuumSubsystem;
    public BetterSubsystem pistonSubsystem;

    Position lastPosition;
    boolean isVacuumOn;
    Position savedPosition;

    public enum Position {
        Stowed(0), Intake(1967), Vertical(349), Horizontal(2733), Max(Horizontal.pos), ClearOfLift(
                Intake.pos);

        final double pos;
        public static final double ticksPerInch = 1;

        private Position(double pos) {
            this.pos = pos;
        }

        public final int toTicks() {
            return (int) (pos * ticksPerInch);
        }
    }

    public Intake() {
        var map = Robot.map.intake;

        BetterTalonSRXConfig config = new BetterTalonSRXConfig();
        config.invert = map.invertIntake();
        config.invertEncoder = map.invertIntakeEncoder();
        config.ticksPerInch = Position.ticksPerInch;
        config.slot0.kP = 4;
        config.slot0.allowableClosedloopError = 5;
        config.motionCruiseVelocity = 300;
        config.motionAcceleration = 300 * 2;
        // config.forwardSoftLimitEnable = true;
        // config.forwardSoftLimitThreshold = Position.Max.toTicks();
        config.openloopRamp = 0.25;
        // config.reverseLimitSwitchNormal = LimitSwitchNormal.NormallyClosed;
        // config.forwardLimitSwitchNormal = LimitSwitchNormal.NormallyClosed;
        config.peakCurrentLimit = 5;
        config.peakCurrentDuration = 50;
        config.continuousCurrentLimit = 2;

        controller = new BetterTalonSRX(map.controllerCanID(), config);

        vacuumController = Mock.createMockable(Talon.class, map.vacuumPort());
        vacuumController.setSafetyEnabled(false);
        vacuumController.setInverted(map.invertVacuum());

        // pistonController = Mock.createMockable(Solenoid.class, map.pistonPCMPort());
        pistonController = new BetterSolenoid(map.pistonPCMPort());

        intakeSubsystem = new BetterSubsystem();
        vacuumSubsystem = new BetterSubsystem();
        pistonSubsystem = new BetterSubsystem();

        lastPosition = Position.Stowed;

        setPosition(Position.Stowed);
        setVacuum(false);
        setPiston(false);
    }

    public void createSendable(SendableMaster master) {
        master.add(controller);
        master.add(new IntakeSendable(this));
        master.add(new ResetIntakeEncoder());
        master.add("Intake Joystick", new ControlIntakeJoystick());
        master.add(new ControlVacuumJoystick());
        master.add("Sol", pistonController);

        Robot.instance.addCommand(new CalibrateIntakeEncoder(), true);
        Robot.instance.addCommand(new SetVacuum(false), true);
    }

    public void resetEncoder() {
        controller.resetEncoder();
    }

    public void setPosition(Position position) {
        this.lastPosition = position;
        controller.setBrakeMode(true);
        controller.setMagic(position.pos);
    }

    public void pushPosition() {
        savedPosition = lastPosition;
    }

    public void popPosition() {
        setPosition(savedPosition);
    }

    public double getActualPosition() {
        return controller.getEncoderPos();
    }

    public void set(double v) {
        controller.setOldPercent(v);
    }

    public boolean isStowed() {
        return lastPosition == Position.Stowed;
    }

    public void setVacuum(boolean on) {
        isVacuumOn = on;
        if (on) {
            vacuumController.set(1.0);
        } else {
            vacuumController.set(0.0);
        }
    }

    public void setPiston(boolean on) {
        pistonController.set(on);
    }

    public void disable() {
        controller.setBrakeMode(false);
    }
}


class IntakeSendable extends SendableBase {
    Intake intake;
    static double ANGLE_ZERO = Intake.Position.Vertical.pos;
    static double ANGLE_NINETY = 2800;
    static double TICKS_PER_ANGLE = (ANGLE_NINETY - ANGLE_ZERO) / 90;

    public IntakeSendable(Intake intake) {
        this.intake = intake;
    }

    double getAngle() {
        return (intake.controller.getEncoderPos() - ANGLE_ZERO) / TICKS_PER_ANGLE;
    }

    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", this::getAngle, null);
        builder.addBooleanProperty("Vacuum", () -> intake.isVacuumOn, null);
    }
}
