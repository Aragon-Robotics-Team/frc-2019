package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.RobotMap;
import frc.robot.commands.lift.ResetLiftEncoder;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;

public class Lift extends Subsystem {
    public BetterTalonSRX controller;

    ShuffleboardTab tab;

    public Lift() {
        BetterTalonSRXConfig config = new BetterTalonSRXConfig();
        config.isConnected = RobotMap.LIFT_INSTALLED;
        config.invert = false;
        config.invertEncoder = true;
        config.ticksPerInch = 254.625;
        config.slot0.kP = 8.0;
        config.slot0.allowableClosedloopError = 25;
        config.motionCruiseVelocity = 150;
        config.motionAcceleration = 300;

        controller = new BetterTalonSRX(RobotMap.LIFT_CAN, config);

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
