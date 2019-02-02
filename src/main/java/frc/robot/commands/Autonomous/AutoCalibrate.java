package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.*;
import java.util.*;
import com.kauailabs.navx.frc.AHRS;

public class AutoCalibrate extends Command implements PIDOutput {
    public int stage;
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
    public final double test_offset = 60;

    public ArrayList<Double> datapoints = new ArrayList<Double>();
    public ArrayList<Double> amplitudes = new ArrayList<Double>();
    public double lastDatapoint;
    public double maxAngle;
    public double minAngle;

    public int lastPeakType = -1;

    public AutoCalibrate(double target) {
        requires(Robot.myNavX);
        requires(Robot.myDrivetrain);
        this.target = target;
    }

    protected void initialize() {
        timer.start();
        Robot.myNavX.ahrs.set;
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

    public void addDataPoint(double d) {
        lastDatapoint = d;
        datapoints.add(d);
    }

    public boolean isPeak(){
        // is lastDatapoint a peak
        if (lastPeakType == -1 && lastDatapoint >= lastLastDatapoint && lastDatapoint >= ){

        }
    }

    public double getLoss() {
        return 0.0;
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


