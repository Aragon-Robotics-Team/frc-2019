package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
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

    public enum Position {
        Stowed(0), Hatch1(0), Port1(15), Hatch2(65), Port2(65), Hatch3(65), Port3(65), Max(Port3.pos);

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
        // config.slot0.kP = 2.0;
        // config.slot0.allowableClosedloopError = 10;
        config.motionCruiseVelocity = 1000;
        config.motionAcceleration = 1000 * 4;
        config.forwardSoftLimitEnable = true;
        config.forwardSoftLimitThreshold = Position.Max.toTicks();
        config.openloopRamp = 0.25;
        config.forwardLimitSwitchNormal = LimitSwitchNormal.Disabled;

        controller = new BetterTalonSRX(map.controllerCanID(), config);

        resetEncoder();
        setPosition(Position.Stowed);
    }

    public void periodic() {
        Robot.myDrivetrain.setSlow(controller.getInch() >= 20);
    }

    public String getTabName() {
        return "Lift";
    }

    public void createSendable(SendableMaster master) {
        master.add(new SendableLift(this));
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

    public void set(double vel) {
        lastPosition = Position.Stowed;
        controller.setOldPercent(vel);
    }

    public boolean isStowed() {
        return lastPosition == Position.Stowed;
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
