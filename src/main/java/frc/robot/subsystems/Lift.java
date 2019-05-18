package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.lift.CalibrateLiftEncoder;
import frc.robot.commands.lift.ControlLiftJoystick;
import frc.robot.commands.lift.ResetLiftEncoder;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSpeedController;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.Disableable;
import frc.robot.util.SendableMaster;

public class Lift extends BetterSubsystem
        implements BetterSendable, BetterSpeedController, Disableable {
    public BetterTalonSRX controller;
    Position lastPosition = Position.Stowed;
    Position oldSavedPosition = Position.Stowed;
    boolean oldInDanger;
    double savedPos = -1;
    private final boolean debugDanger = true;

    public enum Position {
        Stowed(0), Hatch1(0), Port1(3.9375), CargoPort(10.5), Hatch2(13.6875), Port2(
                17.8375), Hatch3(27.6875), Port3(31.9375), Max(28.5), Manual(-1), Paused(-1);

        static final double POINT_OF_DISCONTINUITY = 11.5;
        static final double AREA_OF_INFLUENCE = 2.5;
        static final double WIDE_AREA_OF_INFLUENCE = 2.5;

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
        config.motionCruiseVelocity = 1500;
        config.motionAcceleration = 2000;
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
        // config.peakOutputReverse = -0.5;

        controller = new BetterTalonSRX(map.controllerCanID(), config);
        // controller.talon.overrideLimitSwitchesEnable(false);
        resetEncoder();
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
            String name = "Pos " + pos + " " + pos.pos;
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
        if (debugDanger) {
            System.out.println("Start SetRawPos: " + position + " " + position.pos);
        }
        if (position == Position.Paused) {
            if (debugDanger) {
                System.out.print("SetRawPos: Pause: " + savedPos + "\n");
            }
            // Stop movement for 1 frame
            controller.setOldPercent(0.0);
            (new SetLiftPosition(Position.Manual)).start();
            return;
        }

        double pos;

        if (position == Position.Manual) {
            if (debugDanger && savedPos < 0.0) {
                System.out.println("Tried to go to Manual Pos, but savedPos is not set");
                setPosition(oldSavedPosition);
                return;
            }
            pos = savedPos;
        } else {
            pos = position.pos;
        }
        if (debugDanger) {
            System.out.println("SetRawPos: " + pos);
        }
        this.lastPosition = position;
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

    private boolean inZone(double pos, double POD, double radius) {
        return (Math.abs(pos - POD) <= radius);
    }

    void newCheckInDanger() {
        double pos = getActualPosition();
        // boolean isPaused = (lastPosition == Position.Manual || lastPosition ==
        // Position.Paused);
        boolean isPaused = (savedPos > 0.0);
        if (isPaused && lastPosition != Position.Manual && lastPosition != Position.Paused) {
            System.out.println("Waa");
            return;
        }
        double wantPos = isPaused ? oldSavedPosition.pos : lastPosition.pos;
        double POD = Position.POINT_OF_DISCONTINUITY;
        double radius = Position.AREA_OF_INFLUENCE;
        double largeRadius = Position.WIDE_AREA_OF_INFLUENCE;

        boolean intakeNotOk = Math.min(Robot.myIntake.getActualPosition(),
                Robot.myIntake.lastPosition.pos) < Intake.Position.ClearOfLift.pos;
        boolean wantToCross = (wantPos <= POD) == (POD <= pos);
        // System.out.print(wantToCross + " ");
        wantToCross |= inZone(wantPos, POD, largeRadius);
        // System.out.println(inZone(wantPos, POD, largeRadius) + " " + wantToCross);
        boolean inBadZone = inZone(pos, POD, radius);
        boolean inDanger = (wantToCross || inBadZone);

        // Todo: wantToCross should be "enter danger zone OR cross POD"
        // if Cargo pos is in danger zone, and want to enter, then inDanger = true
        // even if not currently in danger zone or not want to cross POD
        // Should wantToCross include "leave danger zone"?
        // No, because should not pause before leaving if not crossing POD

        StringBuilder b;
        boolean print;
        if (debugDanger) {
            print = false;
            b = new StringBuilder(Double.toString(Timer.getFPGATimestamp()));
            b.append(" Pause: " + isPaused + " Cross: " + wantToCross + " IntakeNotOk: "
                    + intakeNotOk + "\n");
        }
        if (inBadZone) {
            if (wantToCross && intakeNotOk && !isPaused) {
                // If this point is reached, inDanger is true and the intake is/will be moving
                oldSavedPosition = lastPosition;

                if (debugDanger) {
                    b.append("Pausing movement to wait for intake\n");
                    print = true;

                    b.append("Saved: " + oldSavedPosition.pos + "\n");
                    b.append("New:   " + pos);
                }
                setCustomSetpoint(pos);
                setPosition(Position.Paused);
                // b.append("Pos: " + oldSavedPosition.pos + "\n");
            } else if (!intakeNotOk && isPaused) {
                if (debugDanger) {
                    b.append("The Intake is now good, resuming movement\n");
                    print = true;
                }
                savedPos = -1.0;
                setPosition(oldSavedPosition);
            }
        }

        if (inDanger) {
            if (!oldInDanger) {
                if (debugDanger) {
                    b.append("New In Danger\n");
                    print = true;
                }
                Robot.myIntake.pushPosition();
            }
            if ((!oldInDanger || (Robot.myIntake.lastPosition != Intake.Position.WantClearOfLift))
                    && intakeNotOk) {
                // System.out.println("moving out of the way");
                // Robot.myIntake.setPosition(Intake.Position.Intake);

                (new SetIntakePosition(Intake.Position.WantClearOfLift)).start();
            }
        } else {
            if (oldInDanger) {
                if (debugDanger) {
                    b.append("Not In Danger Anymore\n");
                    print = true;
                }
                Robot.myIntake.popPosition();
            }
        }
        if (debugDanger) {
            b.append("\n");

            if (print) {
                System.out.print(b.toString() + "\n");
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

    public void disable() {
        setPosition(Position.Stowed);
        controller.setBrakeMode(false);
        enable();
    }

    public void enable() {
        oldInDanger = false;
        savedPos = -1;
        lastPosition = Position.Stowed;
        oldSavedPosition = Position.Stowed;
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
