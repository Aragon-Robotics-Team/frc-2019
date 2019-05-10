package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.commands.lift.CalibrateLiftEncoder;
import frc.robot.commands.lift.ControlLiftJoystick;
import frc.robot.commands.lift.ResetLiftEncoder;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSpeedController;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.SendableMaster;

public class Lift extends BetterSubsystem implements BetterSendable, BetterSpeedController {
    public BetterTalonSRX controller;
    Position lastPosition = Position.Stowed;
    Position oldSavedPosition = Position.Stowed;
    boolean oldInDanger;
    double savedPos = -1;

    public enum Position {
        Stowed(0), Hatch1(0), Port1(3.9375), CargoPort(10.5), Hatch2(13.6875), Port2(
                17.8375), Hatch3(27.6875), Port3(31.9375), Max(28.5), Manual(-1), Paused(-1);

        static final double POINT_OF_DISCONTINUITY = 11.0;
        static final double AREA_OF_INFLUENCE = 2.0;
        static final double WIDE_AREA_OF_INFLUENCE = 2.0;

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

    public void periodic() {
        // Robot.myDrivetrain.setSlow(controller.getInch() >= 20);
        // checkInDanger();
        newCheckInDanger();
    }

    public String getTabName() {
        return "Lift";
    }

    public void createSendable(SendableMaster master) {
        master.add(new SendableLift(this));
        master.add(controller);
        master.add(new ResetLiftEncoder());
        master.add("Lift Joystick", new ControlLiftJoystick());

        for (Position pos : new Position[] {Position.Stowed, Position.Port1, Position.CargoPort,
                Position.Hatch2, Position.Port2, Position.Hatch3, Position.Port3}) {
            String name = "Pos " + pos.toTicks();
            master.add(name, new SetLiftPosition(pos));
        }

        Robot.instance.addCommand(new CalibrateLiftEncoder(), true);
    }

    public void resetEncoder() {
        controller.resetEncoder();
    }

    public void setCustomSetpoint(double val) {
        savedPos = val;
    }

    public void setPosition(Position position) {
        rawSetPosition(position);
    }

    public void oldSetPosition(Position position) {
        if (position != Position.Manual) {
            boolean crossing = ((lastPosition.pos <= Position.POINT_OF_DISCONTINUITY
                    && position.pos >= Position.POINT_OF_DISCONTINUITY)
                    || (lastPosition.pos >= Position.POINT_OF_DISCONTINUITY
                            && position.pos <= Position.POINT_OF_DISCONTINUITY));

            boolean liftOK = true;
            try {
                liftOK = Robot.myIntake.getActualPosition() > Intake.Position.ClearOfLift.pos;
            } catch (NullPointerException ex) {
            }

            boolean OK = crossing ? liftOK : true;

            System.out.printf("cross: %s intake: %s ok: %s\n", crossing, liftOK, OK);
            System.out.printf("lastpos: %s newpos: %s\n\n", lastPosition.pos, position.pos);

            if (OK) {
                rawSetPosition(position);
            } else {
                rawSetPosition(lastPosition);
                System.out.println("access denied");
            }
        } else {
            rawSetPosition(Position.Manual);
        }
    }

    void rawSetPosition(Position position) {
        this.lastPosition = position;
        double pos;
        if (position == Position.Manual || position == Position.Paused) {
            pos = savedPos / Position.ticksPerInch;
        } else {
            pos = position.pos;
        }
        controller.setMagic(pos);
    }

    public void set(double v) {
        controller.setOldPercent(v);
    }

    public boolean isStowed() {
        return lastPosition == Position.Stowed;
    }

    double getActualPosition() {
        return controller.getInch();
    }

    void newCheckInDanger() {
        double pos = getActualPosition();
        double wantPos = lastPosition.pos;
        double POD = Position.POINT_OF_DISCONTINUITY;
        double radius = Position.AREA_OF_INFLUENCE;
        double error = Math.abs(pos - POD); // current dist from POD

        boolean intakeNotOk = Robot.myIntake.getActualPosition() < Intake.Position.ClearOfLift.pos;
        boolean wantToCross = (wantPos <= POD) == (POD <= pos);
        boolean inBadZone = (error <= radius);
        boolean inDanger = (wantToCross || inBadZone);

        // Todo: wantToCross should be "enter danger zone OR cross POD"
        // if Cargo pos is in danger zone, and want to enter, then inDanger = true
        // even if not currently in danger zone or not want to cross POD
        // Should wantToCross include "leave danger zone"?
        // No, because should not pause before leaving if not crossing POD

        if (inBadZone) {
            if (wantToCross && intakeNotOk && (lastPosition != Position.Paused)) {
                oldSavedPosition = lastPosition;
                setCustomSetpoint(pos);
                setPosition(Position.Paused);
            } else if (!intakeNotOk && (lastPosition == Position.Paused)) {
                setPosition(oldSavedPosition);
            }
        }

        if (inDanger) {
            System.out.println(Timer.getFPGATimestamp() + " in danger!");
            if (!oldInDanger) {
                System.out.println("and it's new");
                Robot.myIntake.pushPosition();
            }
            if (intakeNotOk) {
                System.out.println("moving out of the way");
                Robot.myIntake.setPosition(Intake.Position.Intake);
            }
        } else {
            if (oldInDanger) {
                System.out.println(Timer.getFPGATimestamp() + " not in danger anymore");
                Robot.myIntake.popPosition();
            }
        }

        oldInDanger = inDanger;
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


class SendableLift extends SendableBase {
    Lift lift;

    public SendableLift(Lift lift) {
        this.lift = lift;
    }

    final double getHatch() {
        switch (lift.lastPosition) {
            case Hatch1:
                return 1;
            case Hatch2:
                return 2;
            case Hatch3:
                return 3;
            default:
                return 0;
        }
    }

    final double getPort() {
        switch (lift.lastPosition) {
            case Port1:
                return 1;
            case Port2:
                return 2;
            case Port3:
                return 3;
            default:
                return 0;
        }
    }

    final double getError() {
        final double error = lift.controller.getInch() - lift.lastPosition.pos;
        return error / 15;
    }

    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Hatch", this::getHatch, null);
        builder.addDoubleProperty("Port", this::getPort, null);
        builder.addDoubleProperty("Error", this::getError, null);
    }
}
