package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Pneumatics extends Subsystem {
    public Compressor compressor;
    public Solenoid sol0;

    public Pneumatics() {
        compressor = new Compressor(RobotMap.PneumaticsCan0);
        sol0 = new Solenoid(RobotMap.PneumaticsCan0, RobotMap.PneumaticsSol0);
    }

    public void initDefaultCommand() {
    }

    public void enableCompressor() {
        compressor.setClosedLoopControl(true);
    }

    public void disableCompressor() {
        compressor.setClosedLoopControl(false);
    }

    public void sol0Set(boolean on) {
        System.out.println(on);
        sol0.set(on);
    }
}
