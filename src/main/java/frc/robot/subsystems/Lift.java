package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.commands.lift.ControlLiftJoystick;
import frc.robot.commands.lift.ResetLift;
import frc.robot.commands.lift.ResetLiftEncoder;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.SendableMaster;

public class Lift extends BetterSubsystem implements BetterSendable {
    public BetterTalonSRX controller;

    public enum Position {
        Stowed(0), Hatch1(0), Port1(15), Hatch2(65), Port2(65), Hatch3(65), Port3(65);

        double pos;

        private Position(double pos) {
            this.pos = pos;
        }
    }

    public Lift() {
        var map = Robot.map.lift;

        BetterTalonSRXConfig config = new BetterTalonSRXConfig();
        config.invert = true;
        config.invertEncoder = false;
        config.ticksPerInch = 254.625;
        config.slot0.kP = 8.0;
        config.slot0.allowableClosedloopError = 5;
        config.motionCruiseVelocity = 1000;
        config.motionAcceleration = 1000 * 4;

        controller = new BetterTalonSRX(map.controllerCanID(), config);

        (new ResetLift()).start();
    }

    public String getTabName() {
        return "Lift";
    }

    public void createSendable(SendableMaster master) {
        master.add(controller);
        master.add(new ResetLiftEncoder());
        master.add(new ControlLiftJoystick());
    }

    public void resetEncoder() {
        controller.resetEncoder();
    }

    public void setPosition(Position position) {
        controller.setMagic(position.pos);
    }
}
