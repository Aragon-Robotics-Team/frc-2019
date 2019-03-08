package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.commands.intake.ResetIntakeEncoder;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.Mock;
import frc.robot.util.SendableMaster;

public class Intake extends Subsystem implements BetterSendable {
    public BetterTalonSRX controller;
    Talon vacuumController;
    Solenoid pistonController;

    public enum Position {
        Stowed(0.0), Intake(0.6), Horizontal(1.0), Vertical(0.0);

        double pos;

        private Position(double pos) {
            this.pos = pos;
        }
    }

    public Intake() {
        var map = Robot.map.intake;

        BetterTalonSRXConfig config = new BetterTalonSRXConfig();
        config.invert = true;
        config.invertEncoder = true;
        config.ticksPerInch = 4552;
        config.slot0.kP = 8;
        config.slot0.allowableClosedloopError = 0;
        config.motionCruiseVelocity = 255;
        config.motionAcceleration = 500;

        controller = new BetterTalonSRX(map.controllerCanID(), config);

        vacuumController = Mock.createMockable(Talon.class, map.vacuumPort());
        vacuumController.setSafetyEnabled(false);
        vacuumController.setInverted(true);

        pistonController = Mock.createMockable(Solenoid.class, map.pistonPCMPort());

        setVacuum(false);
        setPosition(Position.Stowed);
    }

    public void createSendable(SendableMaster master) {
        master.add(controller);
        master.add(new ResetIntakeEncoder());
    }

    public void resetEncoder() {
        controller.resetEncoder();
    }

    public void setPosition(Position position) {
        controller.setBrakeMode(true);
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

    public void disable() {
        controller.setBrakeMode(false);
    }

    public void initDefaultCommand() {
    }
}
