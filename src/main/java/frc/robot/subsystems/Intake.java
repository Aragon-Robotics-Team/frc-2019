package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.commands.intake.intake.CalibrateIntakeEncoder;
import frc.robot.commands.intake.intake.ControlIntakeJoystick;
import frc.robot.commands.intake.intake.ResetIntakeEncoder;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.intake.vacuum.ControlVacuumJoystick;
import frc.robot.commands.intake.vacuum.PressureTriggerCommand;
import frc.robot.commands.intake.vacuum.SetVacuum;
import frc.robot.util.BetterDigitalInput;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSolenoid;
import frc.robot.util.BetterSpeedController;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.BetterTrigger;
import frc.robot.util.Disableable;
import frc.robot.util.Mock;
import frc.robot.util.SendableMaster;

public class Intake extends BetterSubsystem implements BetterSendable, Disableable, BetterSpeedController {
    public BetterTalonSRX controller;
    public VictorSPX vacuumController;
    BetterSolenoid pistonController;
    BetterSolenoid hatchController;
    public BetterSubsystem intakeSubsystem;
    public BetterSubsystem vacuumSubsystem;
    public BetterSubsystem pistonSubsystem;
    public Trigger pressureSwitch;

    Position lastPosition = Position.Stowed;
    boolean isVacuumOn;
    Position savedPosition;

    public boolean hasBall = false;

    public enum Position {
        Stowed(0), Intake(2250), Vertical(563), Horizontal(2800), Max(Horizontal.pos), ClearOfLift(570),
        WantClearOfLift(950), Cargo(950);

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
        config.motionCruiseVelocity = 400;
        config.motionAcceleration = 700;
        // config.forwardSoftLimitEnable = true;
        // config.forwardSoftLimitThreshold = Position.Max.toTicks();
        config.openloopRamp = 0.25;
        // config.reverseLimitSwitchNormal = LimitSwitchNormal.NormallyClosed;
        // config.forwardLimitSwitchNormal = LimitSwitchNormal.NormallyClosed;
        config.peakCurrentLimit = 5;
        config.peakCurrentDuration = 50;
        config.continuousCurrentLimit = 2;

        controller = new BetterTalonSRX(map.controllerCanID(), config);

        vacuumController = Mock.createMockable(VictorSPX.class, map.vacuumPort());
        vacuumController.setInverted(map.invertVacuum());

        // pistonController = Mock.createMockable(Solenoid.class, map.pistonPCMPort());
        pistonController = new BetterSolenoid(map.pistonPCMPort());
        hatchController = new BetterSolenoid(map.hatchPCMPort());

        intakeSubsystem = new BetterSubsystem();
        vacuumSubsystem = new BetterSubsystem();
        pistonSubsystem = new BetterSubsystem();

        BetterDigitalInput rawPressureSwitch = new BetterDigitalInput(9, true);
        rawPressureSwitch.debounceTime = 0.1;

        pressureSwitch = new BetterTrigger(rawPressureSwitch, new BetterTrigger(() -> isVacuumOn));
    }

    public void createSendable(SendableMaster master) {
        master.add(controller);
        master.add(new IntakeSendable(this));
        master.add(new ResetIntakeEncoder());
        master.add("Intake Joystick", new ControlIntakeJoystick());
        master.add(new ControlVacuumJoystick());
        master.add("Sol", pistonController);

        master.add("Pressure Switch", pressureSwitch);
        pressureSwitch.whenActive(new PressureTriggerCommand());
        
        master.add("Hatch", hatchController);

        for (Position pos : new Position[] { Position.Stowed, Position.Intake, Position.Vertical, Position.ClearOfLift,
				Position.Cargo }) {
            String name = "Pos " + pos + " " + pos.pos;
            master.add(name, new SetIntakePosition(pos));
        }

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
        (new SetIntakePosition(savedPosition)).start(); // setPosition(savedPosition);
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
            vacuumController.set(VictorSPXControlMode.PercentOutput, 1.0);
        } else {
            vacuumController.set(VictorSPXControlMode.PercentOutput,0.0);
            hasBall = false;
        }
    }

    public void setPiston(boolean on) {
        pistonController.set(on);
    }

    public void setHatch(boolean on) {
        hatchController.set(on);
    }

    public boolean getHatch() {
        return hatchController.get();
    }

    public void enable() {
        disable();
    }

    public void disable() {
        lastPosition = Position.Stowed;

        setPosition(Position.Stowed);
        setVacuum(false);
        setPiston(false);
        setHatch(false);
    }
}

class IntakeSendable extends SendableBase {
    Intake intake;
    static final double ANGLE_ZERO = Intake.Position.Vertical.pos;
    static final double ANGLE_NINETY = Intake.Position.Horizontal.pos;
    static final double TICKS_PER_ANGLE = (ANGLE_NINETY - ANGLE_ZERO) / 90;

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
