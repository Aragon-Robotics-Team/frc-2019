package frc.robot.commands.Autonomous;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;
import java.util.ArrayList;
import java.util.Map;

public class AutoCalibrate extends Command implements PIDOutput {
    // calibrates PID constants.
    // Uses Ziegler–Nichols method:
    // https://en.wikipedia.org/wiki/Ziegler%E2%80%93Nichols_method
    // (accessed 2/4/2019)

    // hyperparameters - stage0
    public final double test_offset = 60; // starting offset of robot (to test oscilations)

    // hyperparameters - stage1
    public final int fail_threshold = 3; // how many amplitudes to consider for failed P-value
    public final int succeed_threshold = 7; // how many successive fail-checks to pass until success
                                            // a check is made each new amplitude, so 4-3
    public final double fail_avg_ratio = .05; // fails if greater than x% gain/loss ratio
    public final double est_max_angle_delta = 10;
    // robot control variables
    public double p = 0.16;
    public double i = 0;
    public double d = 0;
    public double f = 1 / est_max_angle_delta; // feedforward gain. (multiplied by a model written
                                               // in PIDBase)
    public PIDController pidC;
    public double pidOut;
    // control flow variables
    public int stage = -1; // 0 = reset and init, 1 = testing p-value
    public double pmax;
    public double pmin;
    public double failchecks;

    // Data Storage variables
    public Timer timer;
    public double period;
    public ArrayList<Double> datapoints;
    public ArrayList<Double> amplitudes;
    public ArrayList<Double> halfPeriods;
    public double lastDatapoint;
    public double lastPeak;
    public double lastPeakTime;
    public int lastPeakType = -1; // -1 = valley/-angle peak, 1 = peak/+angle peak

    // resulting constants
    public double kp, ki, kd, ku, tu;

    private ShuffleboardTab tab;
    private NetworkTableEntry stageWidget;
    private NetworkTableEntry pidOutWidget;

    public AutoCalibrate() {
        requires(Robot.myNavX);
        requires(Robot.myDrivetrain);
    }

    protected void initialize() {
        tab = Shuffleboard.getTab("AutoCalibrate");
        stageWidget = tab.add("Stage", stage).withWidget(BuiltInWidgets.kTextView).getEntry();
        tab.add("NavX", (Sendable) Robot.myNavX.ahrs);
        Map<String, Object> properties = Map.of("min", -1, "max", 1);
        pidOutWidget = tab.add("PID Output", 0).withWidget(BuiltInWidgets.kDial)
                .withProperties(properties).getEntry();

        stage = -1;
        System.out.println("Stage: -1");
        timer = new Timer();
        timer.reset();
        timer.start();
        Robot.myNavX.ahrs.zeroYaw();
        // 0/360
        pidC = new PIDController(p, i, d, f, Robot.myNavX.ahrs, this);
        Robot.myNavX.ahrs.setPIDSourceType(PIDSourceType.kDisplacement);
        tab.add(pidC);
        pidC.setSetpoint(test_offset);
        pidC.setInputRange(-180.0f, 180.0f);

        pidC.setOutputRange(-1.0, 1.0);
        pidC.setContinuous(true);
        pidC.disable();
    }

    protected void execute() {
        double dp = getAngle(); // datapoint
        switch (stage) {
            case -1:
                // stop/reset robot
                pidC.disable();
                pidC.reset();
                pidC.setPID(p, i, d);
                Robot.myNavX.ahrs.zeroYaw();
                System.out.println("stopping and reseting robot");
                System.out.println("P: " + p + "\nI: " + i + "\nD: " + d);

                // control flow vars
                stage = 0;
                System.out.println("Stage: 0");
                pmax = p;
                pmin = p;
                failchecks = 0;

                // data storage vars
                timer.reset();
                datapoints = new ArrayList<Double>();
                amplitudes = new ArrayList<Double>();
                halfPeriods = new ArrayList<Double>();
                lastDatapoint = test_offset;
                lastPeak = test_offset;
                lastPeakType = -1;
                datapoints.add(test_offset);
                amplitudes.add(lastPeak);

                break;
            case 0:
                // wait out timer
                if (timer.get() >= 2) {
                    pidC.enable();
                    lastPeakTime = timer.get();
                    stage = 1;
                    System.out.println("Stage: 1");
                }
                break;
            case 1:
                // monitor amplitudes
                if (isExtreme(lastDatapoint, dp)) {
                    addAmplitude(lastPeak, lastDatapoint);
                    addHalfPeriod(lastPeakTime, timer.get());
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
                                System.out.println("Stage: -1");
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
                                System.out.println("Stage: -1");
                                break;
                            case 0:
                                failchecks++;
                                if (failchecks == succeed_threshold) {
                                    stage = 2;
                                    System.out.println("Stage: 2");
                                    ku = p;
                                    tu = getAvgPeriod(halfPeriods);
                                    kp = getFinalP(ku);
                                    ki = getFinalI(ku, tu);
                                    kd = getFinalD(ku, tu);
                                    System.out.println("ku: " + ku);
                                    System.out.println("tu: " + tu);
                                    System.out.println("kp: " + kp);
                                    System.out.println("ki: " + ki);
                                    System.out.println("kd: " + kd);
                                }
                        }

                    }
                }
                break;

            case 2:
                // should be be complete
                break;

        }
        addDataPoint(dp);
        stageWidget.setNumber(stage);
    }

    public double getAngle() {
        return rollover(Robot.myNavX.ahrs.getYaw() - test_offset);
    }

    public double rollover(double angle) {
        angle += 180;
        while (angle < 0) {
            angle += 360;
        }
        while (angle > 360) {
            angle -= 360;
        }
        return angle - 180;

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

    public void addHalfPeriod(double lastPeakTime, double currentPeakTime) {
        halfPeriods.add(currentPeakTime - lastPeakTime);
        lastPeakTime = currentPeakTime;
    }

    public boolean isPeak(double lastDataPoint, double dp) {
        // returns whether the last datapoint is a peak, based on it's neighbors and the
        // direction
        // it should be turning
        if (lastPeakType == -1 && lastDatapoint >= dp) {
            return true;
        }
        return false;
    }

    public boolean isValley(double lastDataPoint, double dp) {
        // returns whether the last datapoint is a peak, based on it's neighbors and the
        // direction
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

    public double getAvgPeriod(ArrayList<Double> halfPeriods) {
        double sum = 0;
        for (Double val : halfPeriods) {
            sum += val;
        }
        return sum * 2 / halfPeriods.size();
    }

    public double getFinalP(double ku) {
        return 0.2 * ku;
    }

    public double getFinalI(double ku, double tu) {
        return 0.4 * ku / tu;
    }

    public double getFinalD(double ku, double tu) {
        return ku * tu / 15;
    }

    public boolean isFinished() {
        // placeholder
        if (stage == 2) {
            return true;
        }
        return false;
    }

    public void pidWrite(double output) {
        pidOut = output;
        Robot.myDrivetrain.controlArcade(0, output);
        if (pidOutWidget != null) {
            pidOutWidget.setNumber(output);
        }
    }
}
