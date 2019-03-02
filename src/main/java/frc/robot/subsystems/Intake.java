package frc.robot.subsystems;

import static frc.robot.util.Mock.mock;
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
    public BetterTalonSRX controller;
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
        config.isConnected = RobotMap.INTAKE_INSTALLED;
        config.invert = true;
        config.invertEncoder = true;
        config.ticksPerInch = 4552;
        config.slot0.kP = 8;
        config.slot0.allowableClosedloopError = 0;
        config.motionCruiseVelocity = 255;
        config.motionAcceleration = 500;

        controller = new BetterTalonSRX(RobotMap.INTAKE_CAN, config);

        tab = Shuffleboard.getTab("Intake");
        controller.addShuffleboard(tab, "Intake");
        tab.add(new ResetIntakeEncoder());

        vacuumController =
                RobotMap.INTAKE_VACUUM_INSTALLED ? (new Talon(RobotMap.INTAKE_VACUUM_PWM))
                        : mock(Talon.class);
        vacuumController.setSafetyEnabled(false);
        vacuumController.setInverted(true);

        pistonController =
                new BetterSolenoid(RobotMap.INTAKE_PISTON_PORT, RobotMap.INTAKE_PISTON_INSTALLED);

        setVacuum(false);
        setPosition(Position.Stowed);
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
