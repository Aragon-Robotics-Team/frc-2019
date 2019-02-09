package frc.robot.util;

public class Deadband {
    private double minValue;
    private double m;
    private double deadband;

    public Deadband(double minValue, double deadband) {
        this.minValue = minValue;
        m = calcM(minValue);
        this.deadband = deadband;
    }

    public double calc(double input) {
        if (isDeadband(input)) {
            return 0;
        } else if (input > 0) {
            return m * (input - deadband) + minValue;
        } else {
            return m * (input + deadband) - minValue;
        }
    }

    private double calcM(double minValue) {
        return (1.0 - minValue) / (1 - deadband);
    }

    private boolean isDeadband(double input) {
        return Math.abs(input) <= deadband;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getDeadband() {
        return deadband;
    }
}
