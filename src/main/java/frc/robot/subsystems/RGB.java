package frc.robot.subsystems;

import java.awt.Color;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.RobotMap;

public class RGB extends Subsystem {
    Talon mainController;
    Talon redController;
    Talon blueController;
    Talon greenController;

    float[] color;

    public RGB() {
        mainController = new Talon(RobotMap.RGB_LED_MAIN_PORT);
        redController = new Talon(RobotMap.RGB_LED_RED_PORT);
        blueController = new Talon(RobotMap.RGB_LED_BLUE_PORT);
        greenController = new Talon(RobotMap.RGB_LED_GREEN_PORT);

        color = new float[3];

        var tab = Shuffleboard.getTab("RGB");
        tab.add("RGBController", new RGBSendable(this));
        tab.add("Main", mainController);
        tab.add("Red", redController);
        tab.add("Blue", blueController);
        tab.add("Green", greenController);
    }

    public void setHSV(double h) {
        setHSV(h, 1.0, 1.0);
    }

    public void setHSV(double h, double s, double v) {
        new Color(Color.HSBtoRGB((float) h, (float) s, (float) v)).getRGBColorComponents(color);
        setRGB(color[0], color[1], color[2]);
    }

    public void setRGB(double r, double g, double b) {
        on();
        System.out.println(r + " " + g + " " + b);

        redController.set(1.0 - r);
        blueController.set(1.0 - g);
        greenController.set(1.0 - b);
    }

    public void on() {
        mainController.set(1.0);
    }

    public void off() {
        mainController.set(0.0);
        redController.set(0.0);
        blueController.set(0.0);
        greenController.set(0.0);
    }

    public void initDefaultCommand() {
    }
}


class RGBSendable extends SendableBase {
    RGB rgb;
    double hue;
    boolean on;

    public RGBSendable(RGB rgb) {
        this.rgb = rgb;
        hue = 0;
    }

    public void set(double h) {
        hue = h;
        rgb.setHSV(h);
    }

    public double get() {
        return hue;
    }

    public boolean getOn() {
        return on;
    }

    public void setOn(boolean o) {
        if (o) {
            rgb.on();
        } else {
            rgb.off();
        }
        this.on = o;
    }

    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Hue", this::get, this::set);
        builder.addBooleanProperty("On", this::getOn, this::setOn);
    }
}
