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

    // hyperparameters - stage0
    public final double test_offset = 60; // starting offset of robot (to test oscilations)

    // hyperparameters - stage1
    public final int fail_threshold = 3; // how many amplitudes to consider for failed P-value
    public final int succeed_threshold = 4; // how many successive fail-checks to pass until success
                                            // a check is made each new amplitude, so 4-3
    public final double fail_avg_ratio = .05; // fails if greater than x% gain/loss ratio

    // robot control variables
    public double p = 0.02;
    public double i = 0;
    public double d = 0;
    public PIDController pidC;
    public double pidOut;

    // control flow variables
    public int stage = 0; // 0 = reset and init, 1 = testing p-value
    public double pmax = p;
    public double pmin = p;
    public double failchecks = 0;

    // Data Storage variables    
    public Timer timer;
    public double period;
    public double error;
    public double target = 0;
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
            case -1:
                // stop robot
                timer.reset();
                pidC = new PIDController(0, 0, 0, Robot.myNavX.ahrs, this);
                stage = 0;
                break;
            case 0:
                // reset robot to test new P value
                if(timer.hasPeriodPassed(1000))
                break;
            case 1:
                // monitor amplitudes
                if (isExtreme(lastDatapoint, dp)) {
                    addAmplitude(lastPeak, lastDatapoint);

                    // use binary search to iterate towards optimal-P
                    if (amplitudes.size() > fail_threshold) {
                        int failstate = isFailedP(amplitudes, fail_threshold, fail_avg_ratio);
                        switch (failstate) {
                            case 1:
                                pmax = p;
                                if (p == pmin) {
                                    pmin /= 2;
                                    p = pmin;
                                    stage = -1; // set and try new p-value
                                } else {
                                    p = (pmin + pmax) / 2;
                                    stage = -1; // reset and try new p-value

                                }
                                break;
                            case -1:
                                pmin = p;
                                if (p == pmax) {
                                    pmax *= 2;
                                    p = pmax;
                                    stage = -1; // reset and try new p-value
                                } else {
                                    p = (pmin + pmax) / 2;
                                    stage = -1; // reset and try new p-value
                                }
                                break;
                            case 0:
                                failchecks++;
                                if (failchecks == succeed_threshold) {
                                    stage = 2;
                                    System.out.println("Ku (ultimate gain) = " + p);
                                }
                        }

                    }
                }
                break;

        }
        addDataPoint(dp);
    }

    public void recordAngle() {

    }

    public double getAngle() {
        return (Robot.myNavX.ahrs.getYaw() + test_offset);
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

    public int isFailedP(ArrayList<Double> amplitudes, int fail_threshold, double fail_avg_ratio) {
        // 1 = amplitudes growing, -1 = shrinking amplitudes, 0 = stable amplitudes?
        int size = amplitudes.size();
        double sum = 0;
        for (int i = 0; i < (size - fail_threshold) - 1; i++) {
            sum += amplitudes.get(i) / amplitudes.get(i + 1);
        }
        sum /= (size - 1);
        if (sum > 1 + fail_avg_ratio) {
            return 1;
        } else if (sum < 1 - fail_avg_ratio) {
            return -1;
        }
        return 0;
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


