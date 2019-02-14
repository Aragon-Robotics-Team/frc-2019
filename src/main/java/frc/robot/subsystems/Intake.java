package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.RobotMap;
import frc.robot.commands.intake.ResetIntakeEncoder;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.PIDGains;

public class Intake extends Subsystem {
    BetterTalonSRX controller;
    Talon vacuumController;

    ShuffleboardTab tab;

    public enum Position {
        Stowed(0.0), Intake(0.6), Horizontal(1.0);

        double pos;

        private Position(double pos) {
            this.pos = pos;
        }
    }

    public Intake() {
        controller = new BetterTalonSRX(RobotMap.IntakeCan, true, true);
        controller.ticksPerInch = 4111;

        PIDGains gains = new PIDGains();
        gains.kP = 8;
        gains.kV = 225;
        gains.kA = 500;
        gains.maxError = 5;

        controller.setPID(gains);

        tab = Shuffleboard.getTab("Intake");
        controller.addShuffleboard(tab, "Intake");
        tab.add(new ResetIntakeEncoder());

        vacuumController = new Talon(RobotMap.VacuumPwm);
        vacuumController.setSafetyEnabled(false);

        off();
        setPos(Position.Stowed);
    }

    public void resetEncoder() {
        controller.resetEncoder();
    }

    public void setPos(Position pos) {
        controller.setMagic(pos.pos);
    }

    public void on() {
        vacuumController.set(1.0);
    }

    public void off() {
        vacuumController.set(0.0);
    }

    public void initDefaultCommand() {
    }
}
