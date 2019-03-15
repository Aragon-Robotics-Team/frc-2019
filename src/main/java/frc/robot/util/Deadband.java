package frc.robot.util;

public class Deadband {
    private double minValue;
    private double m;
    private double deadband;

    public Deadband(double minValue, double deadband) {
        this.minValue = minValue;
        m = calcM(minValue, deadband);
        this.deadband = deadband;
    }

    public double calc(double input) {
        return calc(input, false);
    }

    public double calc(double input, boolean square) {
        double output;

        if (isDeadband(input)) {
            output = 0;
        } else if (input > 0) {
            output = m * (input - deadband) + minValue;
        } else {
            output = m * (input + deadband) - minValue;
        }

        if (square) {
            output = Math.copySign(output * output, output);
        }

        return output;
    }

    private static double calcM(double minValue, double deadband) {
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
