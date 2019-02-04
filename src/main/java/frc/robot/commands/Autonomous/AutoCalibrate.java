package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import java.util.ArrayList;
import com.kauailabs.navx.frc.AHRS;

public class AutoCalibrate extends Command implements PIDOutput {
    // calibrates PID constants.
    // Uses Ziegler–Nichols method: https://en.wikipedia.org/wiki/Ziegler%E2%80%93Nichols_method
    // (accessed 2/4/2019)

    // hyperparameters
    public final double test_offset = 60; // starting offset of robot (to test oscilations)
    // stage 0
    public final int failThreshold = 3; // how many amplitudes to consider for failed P-value
    public final int succeedThreshold = 10; // how many amplitudes to consider for successful
                                            // P-value
    public final int failAvgRatio = .05 // fails if greater than x% gain/loss ratio

    public int stage = 0; // 0 = tuning P to steady oscillations
    public double p = 0.02;
    public double i = 0;
    public double d = 0;

    public double rotCounter = 0;
    public double period;
    public double error;
    public double target = 0;
    public Timer timer;

    public PIDController pidC;
    public double pidOut;

    public ArrayList<Double> datapoints = new ArrayList<Double>();
    public ArrayList<Double> amplitudes = new ArrayList<Double>();
    public double lastDatapoint;
    public double lastPeak = test_offset;



    public int lastPeakType = -1; // -1 = valley/-angle peak, 1 = peak/+angle peak

    public AutoCalibrate(double target) {
        requires(Robot.myNavX);
        requires(Robot.myDrivetrain);
        this.target = target;
    }

    protected void initialize() {
        amplitudes.add(lastPeak);
        timer.start();
        Robot.myNavX.ahrs.zeroYaw();
        // 0/360
        pidC = new PIDController(p, i, d, Robot.myNavX.ahrs, this);
        pidC.setSetpoint(target);
        pidC.setInputRange(-180.0f, 180.0f);
        pidC.setOutputRange(-5.0, 5.0);
        pidC.setContinuous(true);
        pidC.disable();
        pidC.enable();
    }

    protected void execute() {
        double dp = getAngle(); // datapoint
        switch (stage) {
            case 0:
                // monitor amplitudes
                if (isExtreme(lastDatapoint, dp)) {
                    addAmplitude(lastPeak, lastDatapoint);
                }
                if (amplitudes.size() > failThreshold) {

                }
                break;
        }
        addDataPoint(dp);
    }

    public void recordAngle() {

    }

    public double getAngle() {
        return (Robot.myNavX.ahrs.getYaw() + angleOffset);
    }

    // public double getAngleUnlooped() {
    // double x = getAngle() + 360*rotCounter;
    // if(Math.abs(lastDataPoint-x) > 270 ){
    // if (lastDataPoint - x > 0){
    // rotCounter += 1;
    // x += 360;
    // }else{
    // rotCounter -= 1;
    // x -= 360;
    // }
    // }
    // }

    public void addDataPoint(double dp) {
        lastDatapoint = d;
        datapoints.add(d);
    }

    public void addAmplitude(double lastPeak, double currentPeak) {
        amplitudes.add(Math.abs(lastPeak - currentPeak) / 2);
        lastPeak = currentPeak;
    }

    public boolean isPeak(double lastDataPoint, double dp) {
        // returns whether the last datapoint is a peak, based on it's neighbors and the direction
        // it should be turning
        if (lastPeakType == -1 && lastDatapoint >= dp) {
            return true;
        }
        return false;
    }

    public boolean isValley(double lastDataPoint, double dp) {
        // returns whether the last datapoint is a peak, based on it's neighbors and the direction
        // it should be turning
        if (lastPeakType == 1 && lastDatapoint <= dp) {
            return true;
        }
        return false;
    }

    public boolean isExtreme(double lastDataPoint, double dp) {
        if (lastPeakType == 1) {
            return isPeak(lastDataPoint, dp);
        }
        return isValley(lastDataPoint, dp);
    }

    public double getLoss() {
        return 0.0;
    }

    public boolean isFailedP() {
        int size = amplitudes.size();
        double sum = 0;
        for (int i = 0; i < (size - failThreshold) - 1; i++) {
            sum += amplitudes.get(i) / amplitudes.get(i + 1);
        }
        sum /= (size - 1);
        if (sum > 1 + failAvgRatio || sum < 1 - failAvgRatio) {
            return true;
        }
        return false;
    }

    public boolean isFinished() {
        // placeholder
        return true;
    }

    public void pidWrite(double output) {
        pidOut = output;
        Robot.myDrivetrain.controlArcade(0, output);
    }
}


