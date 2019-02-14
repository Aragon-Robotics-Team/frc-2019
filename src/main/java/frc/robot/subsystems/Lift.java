package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.RobotMap;
import frc.robot.commands.lift.ResetLiftEncoder;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.PIDGains;

public class Lift extends Subsystem {
    BetterTalonSRX controller;

    ShuffleboardTab tab;

    public Lift() {
        controller = new BetterTalonSRX(RobotMap.LiftCan, false, true);
        controller.ticksPerInch = 254.625;

        PIDGains gains = new PIDGains();
        gains.kP = 8;
        gains.kV = 150;
        gains.kA = 300;
        gains.maxError = 25;

        controller.setPID(gains);

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
}
