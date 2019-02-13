package frc.robot.subsystems;

import java.util.Map;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.RobotMap;
import frc.robot.util.BetterTalonSRX;

public class Drivetrain extends Subsystem {
    static double speedModifer = -1.0;

    BetterTalonSRX LeftWheels;
    BetterTalonSRX RightWheels;

    public NetworkTableEntry rampSet;

    public Drivetrain() {
        LeftWheels = new BetterTalonSRX(RobotMap.LeftWheelsCan, false);
        RightWheels = new BetterTalonSRX(RobotMap.RightWheelsCan, true);
        // ShuffleboardTab tab = Shuffleboard.getTab("Drive");
        // LeftWheels.addShuffleboard(tab, "Left Wheels");
        // RightWheels.addShuffleboard(tab, "Right Wheels");

        // rampSet =
        // Shuffleboard.getTab("Drive").add("Ramp", 0).withWidget(BuiltInWidgets.kNumberSlider)
        // .withProperties(Map.of("Min", 0, "Max", 5)).getEntry();
    }

    public void initDefaultCommand() {
    }

    public void control(double x, double y) {
        LeftWheels.set(x);
        RightWheels.set(y);
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
