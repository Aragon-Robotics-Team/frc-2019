package frc.robot.subsystems;

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


    public enum Position {
        Stowed(0), Hatch1(0), Port1(15), Hatch2(65), Port2(65), Hatch3(65), Port3(65), Max(
                Port3.pos);

        static final double POINT_OF_DISCONTINUITY = -1;
        static final double AREA_OF_INFLUENCE = -1;
        static final double WIDE_AREA_OF_INFLUENCE = -1;

        final double pos;
        public static final double ticksPerInch = 254.625;

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
        config.slot0.kP = 4.0;
        config.slot0.allowableClosedloopError = 10;
        config.motionCruiseVelocity = 1000;
        config.motionAcceleration = 1000 * 4;
        config.forwardSoftLimitEnable = true;
        config.forwardSoftLimitThreshold = Position.Max.toTicks();
        config.openloopRamp = 0.25;
        // config.forwardLimitSwitchNormal = LimitSwitchNormal.NormallyClosed;
        config.forwardSoftLimitEnable = false;
        config.reverseSoftLimitEnable = false;

        controller = new BetterTalonSRX(map.controllerCanID(), config);
        controller.talon.overrideLimitSwitchesEnable(false);
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

    public void setPosition(Position position) {
        this.lastPosition = position;
        controller.setMagic(position.pos);
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
