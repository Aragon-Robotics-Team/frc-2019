package frc.robot.subsystems;

import java.util.Map;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.RobotMap;
import frc.robot.util.BetterTalonSRX;
import frc.robot.util.BetterTalonSRXConfig;

public class Drivetrain extends Subsystem {
    BetterTalonSRX leftController;
    BetterTalonSRX rightController;

    ShuffleboardTab tab;

    public NetworkTableEntry rampSet;

    public Drivetrain() {
        BetterTalonSRXConfig leftConfig = new BetterTalonSRXConfig();
        leftController = new BetterTalonSRX(RobotMap.LeftWheelsCan, leftConfig);

        BetterTalonSRXConfig rightConfig = new BetterTalonSRXConfig();
        rightConfig.invert = true;
        rightController = new BetterTalonSRX(RobotMap.RightWheelsCan, rightConfig);

        tab = Shuffleboard.getTab("Drive");
        leftController.addShuffleboard(tab, "Left Wheels");
        rightController.addShuffleboard(tab, "Right Wheels");
    }

    public void initDefaultCommand() {
    }

    public void control(double x, double y) {
        leftController.set(x);
        rightController.set(y);
        // System.out.println("Control: " + x + " " + y);
    }

    public void controlArcade(double x, double y) { // x is up/down; y is right/left
        // differentialDrive.arcadeDrive(x, y, false);

        // double rp = 0;
        // double lp = 0;
        // rp += x;
        // lp += x;
        // rp -= y;
        // lp += y;
        // control(lp, rp);

        // Implementation stolen from DifferentialDrive.class WPILib

        double maxInput = Math.copySign(Math.max(Math.abs(x), Math.abs(y)), x);

        if (x * y >= 0.0) { // If both sign are the same
            control(maxInput, x - y);
        } else {
            control(x + y, maxInput);
        }
    }

    public void goForward(double x) {
        control(x, x);
        // System.out.println("Control: " + x);
    }

    public void stop() {
        control(0, 0);
        // System.out.println("stop");
    }

    public void setOpenloopRamp(double ramp) {
        LeftWheels.configOpenloopRamp(ramp);
        RightWheels.configOpenloopRamp(ramp);
    }
}
