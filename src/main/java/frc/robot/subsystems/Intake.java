package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Intake extends Subsystem {
    Talon controller;

    public Intake() {
        controller = new Talon(RobotMap.VacuumPwm);
        controller.setSafetyEnabled(false);

        off();
    }

    public void on() {
        controller.set(1.0);
    }

    public void off() {
        controller.set(0.0);
    }

    public void initDefaultCommand() {
    }
}
