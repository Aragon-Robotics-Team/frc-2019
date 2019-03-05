package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;
import frc.robot.commands.lift.ResetLiftEncoder;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;

public class Lift extends Subsystem {
    public BetterTalonSRX controller;

    ShuffleboardTab tab;

    public enum Position {
        HATCH_1(0.0), PORT_1(8.0), HATCH_2(28.0), PORT_2(0.0), HATCH_3(0.0), PORT_3(0.0);

        double pos;

        private Position(double pos) {
            this.pos = pos;
        }
    }

    public Lift() {
        var map = Robot.map.lift;

        BetterTalonSRXConfig config = new BetterTalonSRXConfig();
        config.invert = false;
        config.invertEncoder = true;
        config.ticksPerInch = 254.625;
        config.slot0.kP = 8.0;
        config.slot0.allowableClosedloopError = 25;
        config.motionCruiseVelocity = 150;
        config.motionAcceleration = 300;

        controller = new BetterTalonSRX(map.controllerCanID(), config);

        tab = Shuffleboard.getTab("Lift");
        controller.addShuffleboard(tab, "Lift");
        tab.add(new ResetLiftEncoder());

        controller.setMagic(0);
    }

    public void initDefaultCommand() {
    }

    public void resetEncoder() {
        controller.resetEncoder();
    }

    public void setPosition(Position position) {
        controller.setMagic(position.pos);
    }
}
