package frc.robot.subsystems;

import java.awt.Color;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.RobotMap;
import frc.robot.commands.rgb.HueCycle;
import frc.robot.commands.rgb.TriangleWave;

public class RGB extends Subsystem {
    Talon mainController;
    Talon redController;
    Talon blueController;
    Talon greenController;

    boolean on;
    float[] hsv = new float[3];
    float[] rgb = new float[3];
    double brightness = 1.0;

    ShuffleboardTab tab;

    public RGB() {
        mainController = new Talon(RobotMap.RGB_LED_MAIN_PORT);
        redController = new Talon(RobotMap.RGB_LED_RED_PORT);
        blueController = new Talon(RobotMap.RGB_LED_BLUE_PORT);
        greenController = new Talon(RobotMap.RGB_LED_GREEN_PORT);

        tab = Shuffleboard.getTab("RGB");
        tab.add("RGBController", new RGBSendable(this));
        tab.add("Main", mainController);
        tab.add("Red", redController);
        tab.add("Blue", blueController);
        tab.add("Green", greenController);

        disable();
    }

    public void init() {
        tab.add(new HueCycle(10.0));
        tab.add(new TriangleWave(60.0 / 125.0));
    }

    // HSV

    public void setHSV(float h) {
        setHSV(new float[] {h, 1.0f, 1.0f});
    }

    public void setHSV(float[] hsv) {
        this.hsv = hsv;

        Color.getHSBColor(hsv[0], hsv[1], hsv[2]).getRGBColorComponents(rgb);
        setRawRGB();
    }

    // RGB

    public void setRGB(float[] rgb) {
        setOn(true);
        hsv = new float[3];

        this.rgb = rgb;
        setRawRGB();
    }

    private void setRawRGB() {
        redController.set(1.0 - rgb[0]);
        blueController.set(1.0 - rgb[1]);
        greenController.set(1.0 - rgb[2]);
    }

    // Double

    public void setHSV(double h) {
        setHSV((float) h);
    }

    public void setHSV(double[] hsv) {
        setHSV(new float[] {(float) hsv[0], (float) hsv[1], (float) hsv[2]});
    }

    public void setRGB(double[] rgb) {
        setRGB(new float[] {(float) rgb[0], (float) rgb[1], (float) rgb[2]});
    }

    // On - Off

    public void setOn(boolean on) {
        this.on = on;
        if (on) {
            mainController.set(brightness);
        } else {
            mainController.set(0.0);
        }
    }

    public boolean isOn() {
        return on;
    }

    public void disable() {
        rgb = new float[3];

        setOn(false);

        redController.set(0.0);
        blueController.set(0.0);
        greenController.set(0.0);
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
        setOn(on);
    }

    public void initDefaultCommand() {
    }
}


class RGBSendable extends SendableBase {
    RGB rgb;

    public RGBSendable(RGB rgb) {
        this.rgb = rgb;
    }

    public void initSendable(SendableBuilder builder) {
        builder.addBooleanProperty("On", rgb::isOn, rgb::setOn);

        builder.addDoubleProperty("Red", (() -> (double) rgb.rgb[0]),
                ((r) -> rgb.setRGB(new float[] {(float) r, rgb.rgb[1], rgb.rgb[2]})));
        builder.addDoubleProperty("Blue", (() -> (double) rgb.rgb[1]),
                ((b) -> rgb.setRGB(new float[] {rgb.rgb[0], (float) b, rgb.rgb[2]})));
        builder.addDoubleProperty("Green", (() -> (double) rgb.rgb[2]),
                ((g) -> rgb.setRGB(new float[] {rgb.rgb[0], rgb.rgb[1], (float) g})));

        builder.addDoubleProperty("Hue", (() -> (double) rgb.hsv[0]),
                ((h) -> rgb.setHSV(new float[] {(float) h, rgb.hsv[1], rgb.hsv[2]})));
        builder.addDoubleProperty("Sat", (() -> (double) rgb.hsv[1]),
                ((s) -> rgb.setHSV(new float[] {rgb.hsv[0], (float) s, rgb.hsv[2]})));
        builder.addDoubleProperty("Val", (() -> (double) rgb.hsv[2]),
                ((v) -> rgb.setHSV(new float[] {rgb.hsv[0], rgb.hsv[1], (float) v})));
    }
}