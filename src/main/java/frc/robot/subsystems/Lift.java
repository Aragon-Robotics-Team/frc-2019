package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.RobotMap;
import frc.robot.util.BetterTalonSRX;

public class Lift extends Subsystem {
    BetterTalonSRX controller;

    public Lift() {
        controller = new BetterTalonSRX(RobotMap.LiftCan);
        controller.addShuffleboard(Shuffleboard.getTab("Lift"), "Lift");
    }

    public void initDefaultCommand() {
    }
}
