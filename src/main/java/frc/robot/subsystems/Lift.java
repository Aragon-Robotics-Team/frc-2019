package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import frc.robot.Robot;
import frc.robot.commands.lift.CalibrateLiftEncoder;
import frc.robot.commands.lift.ControlLiftJoystick;
import frc.robot.commands.lift.ResetLiftEncoder;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSpeedController;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.SendableMaster;

public class Lift extends BetterSubsystem implements BetterSendable, BetterSpeedController {
    public BetterTalonSRX controller;
    Position lastPosition;
    boolean oldInDanger;
    double savedPos;

    public enum Position {
        Stowed(0), Hatch1(0), Port1(2), Hatch2(4), Port2(6), Hatch3(8), Port3(10), Max(28), Manual(
                -1);

        static final double POINT_OF_DISCONTINUITY = -1;
        static final double AREA_OF_INFLUENCE = -1;
        static final double WIDE_AREA_OF_INFLUENCE = -1;

        final double pos;
        // Ticks per inch measured on the first layer outside the stationary part of the
        // 2-stage lift, aka not the actual part of the intake.
        // The actual intake ticksPerInch should be exactly 2x this one.
        // Also, the intake is 19 5/8 inch above the ground measured at the center of
        // the pison, at lift pos=0, at intake vertical.
        public static final double ticksPerInch = 1040.3;

        private Position(double pos) {
            this.pos = pos;
        }

        public final int toTicks() {
            return (int) (pos * ticksPerInch);
        }
    }

    public Lift() {
        var map = Robot.map.lift;

        BetterTalonSRXConfig config = new BetterTalonSRXConfig();
        config.invert = map.invertLift();
        config.invertEncoder = map.invertLiftEncoder();
        config.ticksPerInch = Position.ticksPerInch;
        config.slot0.kP = 2.0;
        // config.slot0.kI = 0.01;
        // config.slot0.integralZone = 80;
        config.slot0.allowableClosedloopError = 5;
        config.motionCruiseVelocity = 1000;
        config.motionAcceleration = 1000;
        config.forwardSoftLimitEnable = true;
        config.forwardSoftLimitThreshold = Position.Max.toTicks();
        config.openloopRamp = 0.25;
        config.forwardLimitSwitchSource = LimitSwitchSource.Deactivated;
        config.forwardLimitSwitchNormal = LimitSwitchNormal.Disabled;
        // config.forwardLimitSwitchNormal = LimitSwitchNormal.NormallyClosed;
        // config.forwardSoftLimitEnable = false;
        // config.reverseSoftLimitEnable = false;
        config.peakCurrentLimit = 10;
        config.peakCurrentDuration = 500;
        config.continuousCurrentLimit = 7;
        config.peakOutputReverse = -0.5;

        controller = new BetterTalonSRX(map.controllerCanID(), config);
        // controller.talon.overrideLimitSwitchesEnable(false);
        resetEncoder();
        setPosition(Position.Stowed);
    }

    // public void periodic() {
    // Robot.myDrivetrain.setSlow(controller.getInch() >= 20);
    // checkInDanger();
    // }

    public String getTabName() {
        return "Lift";
    }

    public void createSendable(SendableMaster master) {
        master.add(controller);
        master.add(new ResetLiftEncoder());
        master.add("Lift Joystick", new ControlLiftJoystick());

        Robot.instance.addCommand(new CalibrateLiftEncoder(), true);
    }

    public void resetEncoder() {
        controller.resetEncoder();
    }

    public void setCustomSetpoint(double val) {
        savedPos = val;
    }

    public void setPosition(Position position) {
        this.lastPosition = position;
        double pos;
        if (position == Position.Manual) {
            pos = savedPos / Position.ticksPerInch;
        } else {
            pos = position.pos;
        }
        controller.setMagic(pos);
    }

    public void set(double v) {
        lastPosition = Position.Stowed;
        controller.setOldPercent(v);
    }

    public boolean isStowed() {
        return lastPosition == Position.Stowed;
    }

    double getActualPosition() {
        return controller.getInch();
    }

    void checkInDanger() {
        double pos = getActualPosition();
        double rawError = pos - Position.POINT_OF_DISCONTINUITY;
        double error = Math.abs(rawError);
        boolean isAbove = rawError > 0;
        boolean inDanger = false;

        if (error < Position.AREA_OF_INFLUENCE) {
            inDanger = true;
        } else if (error < Position.WIDE_AREA_OF_INFLUENCE) {
            double vel = controller.getEncoderRate();
            if (Math.abs(vel) >= 2) {
                inDanger = ((vel > 0) == isAbove);
            }
        }

        if (inDanger) {
            if (!oldInDanger) {
                Robot.myIntake.pushPosition();
            }
            if (Robot.myIntake.getActualPosition() < Intake.Position.ClearOfLift.pos) {
                Robot.myIntake.setPosition(Intake.Position.ClearOfLift);
            }
        } else {
            if (oldInDanger) {
                Robot.myIntake.popPosition();
            }
        }

        oldInDanger = inDanger;
    }
}
