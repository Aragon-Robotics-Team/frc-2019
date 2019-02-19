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

    boolean on;
    float[] hsv = new float[3];
    float[] rgb = new float[3];

    public RGB() {
        mainController = new Talon(RobotMap.RGB_LED_MAIN_PORT);
        redController = new Talon(RobotMap.RGB_LED_RED_PORT);
        blueController = new Talon(RobotMap.RGB_LED_BLUE_PORT);
        greenController = new Talon(RobotMap.RGB_LED_GREEN_PORT);

        var tab = Shuffleboard.getTab("RGB");
        tab.add("RGBController", new RGBSendable(this));
        tab.add("Main", mainController);
        tab.add("Red", redController);
        tab.add("Blue", blueController);
        tab.add("Green", greenController);

        disable();
    }

    // HSV

    public void setHSV(float h) {
        setHSV(new float[] {h, 1.0f, 1.0f});
    }

    public void setHSV(float[] hsv) {
        this.hsv = hsv;

        Color.getHSBColor(hsv[0], hsv[0], hsv[0]).getRGBColorComponents(rgb);
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
            mainController.set(1.0);
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

    public void initDefaultCommand() {
    }
}


class RGBSendable extends SendableBase {
    RGB rgb;

    public RGBSendable(RGB rgb) {
        this.rgb = rgb;
    }

    public double[] getRGB() {
        return new double[] {rgb.rgb[0], rgb.rgb[1], rgb.rgb[2]};
    }

    public double[] getHSV() {
        return new double[] {rgb.hsv[0], rgb.hsv[1], rgb.hsv[2]};
    }

    public void initSendable(SendableBuilder builder) {
        builder.addBooleanProperty("On", rgb::isOn, rgb::setOn);
        builder.addDoubleArrayProperty("HSV", this::getRGB, rgb::setHSV);
        builder.addDoubleArrayProperty("RGB", this::getHSV, rgb::setRGB);
    }
}
