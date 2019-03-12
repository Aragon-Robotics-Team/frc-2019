package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import frc.robot.Robot;
import frc.robot.commands.intake.ResetIntake;
import frc.robot.commands.intake.intake.ControlIntakeJoystick;
import frc.robot.commands.intake.intake.ResetIntakeEncoder;
import frc.robot.commands.intake.vacuum.ControlVacuumJoystick;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSolenoid;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;
import frc.robot.util.Disableable;
import frc.robot.util.Mock;
import frc.robot.util.SendableMaster;

public class Intake extends BetterSubsystem implements BetterSendable, Disableable {
    public BetterTalonSRX controller;
    public Talon vacuumController;
    BetterSolenoid pistonController;
    public BetterSubsystem intakeSubsystem;
    public BetterSubsystem vacuumSubsystem;
    public BetterSubsystem pistonSubsystem;

    public enum Position {
        Stowed(0), Intake(2106), Horizontal(3248), Vertical(590);

        double pos;

        private Position(double pos) {
            this.pos = pos;
        }
    }

    public Intake() {
        var map = Robot.map.intake;

        BetterTalonSRXConfig config = new BetterTalonSRXConfig();
        config.invert = true;
        config.invertEncoder = false;
        config.ticksPerInch = 1;
        config.slot0.kP = 4;
        config.slot0.allowableClosedloopError = 5;
        config.motionCruiseVelocity = 300;
        config.motionAcceleration = 300 * 2;

        controller = new BetterTalonSRX(map.controllerCanID(), config);

        vacuumController = Mock.createMockable(Talon.class, map.vacuumPort());
        vacuumController.setSafetyEnabled(false);
        vacuumController.setInverted(true);

        // pistonController = Mock.createMockable(Solenoid.class, map.pistonPCMPort());
        pistonController = new BetterSolenoid(map.pistonPCMPort());

        intakeSubsystem = new BetterSubsystem();
        vacuumSubsystem = new BetterSubsystem();
        pistonSubsystem = new BetterSubsystem();

        (new ResetIntake()).start();
    }

    public void createSendable(SendableMaster master) {
        master.add(controller);
        master.add(new ResetIntakeEncoder());
        master.add(new ControlIntakeJoystick());
        master.add(new ControlVacuumJoystick());
    }

    public void resetEncoder() {
        // controller.resetEncoder();
    }

    public void setPosition(Position position) {
        controller.setBrakeMode(true);
        // controller.setMagic(position.pos);
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
}
