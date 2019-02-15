package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.RobotMap;
import frc.robot.commands.intake.ResetIntakeEncoder;
import frc.robot.util.BetterSolenoid;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;

public class Intake extends Subsystem {
    BetterTalonSRX controller;
    Talon vacuumController;
    BetterSolenoid pistonController;

    ShuffleboardTab tab;

    public enum Position {
        Stowed(0.0), Intake(0.6), Horizontal(1.0);

        double pos;

        private Position(double pos) {
            this.pos = pos;
        }
    }

    public Intake() {
        BetterTalonSRXConfig config = new BetterTalonSRXConfig();
        config.invert = true;
        config.invertEncoder = true;
        config.ticksPerInch = 4111;
        config.slot0.kP = 8;
        config.slot0.allowableClosedloopError = 5;
        config.motionCruiseVelocity = 255;
        config.motionAcceleration = 500;

        controller = new BetterTalonSRX(RobotMap.INTAKE_CAN, config);

        tab = Shuffleboard.getTab("Intake");
        controller.addShuffleboard(tab, "Intake");
        tab.add(new ResetIntakeEncoder());

        vacuumController = new Talon(RobotMap.INTAKE_VACUUM_PWM);
        vacuumController.setSafetyEnabled(false);

        pistonController = new BetterSolenoid(RobotMap.INTAKE_PISTON);

        setVacuum(false);
        setPosition(Position.Stowed);
    }

    public void resetEncoder() {
        controller.resetEncoder();
    }

    public void setPosition(Position position) {
        controller.setMagic(position.pos);
    }

    public void setVacuum(boolean on) {
        if (on) {
            vacuumController.set(1.0);
        } else {
            vacuumController.set(0.0);
        }
    }

    public void setPiston(boolean on) {
        pistonController.set(on);
    }

    public void initDefaultCommand() {
    }
}
