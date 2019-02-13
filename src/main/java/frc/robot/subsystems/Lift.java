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

        PIDGains gains = new PIDGains();
        gains.kP = 2.00687;
        gains.kV = 150;
        gains.kA = 300;

        tab = Shuffleboard.getTab("Lift");
        controller.addShuffleboard(tab, "Lift");
        tab.add(new ResetLiftEncoder());
    }

    public void initDefaultCommand() {
    }

    public void resetEncoder() {
        controller.resetEncoder();
    }

    public void setVelocity(double velocity) {
        // controller.set(velocity);
        double position = ((velocity + 1) * 61111) / 2;
        System.out.println(position);
        controller.setMagic(position);
    }
}
