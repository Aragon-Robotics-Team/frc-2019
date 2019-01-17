package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Pneumatics extends Subsystem {
    public Compressor compressor;

    public Pneumatics() {
        compressor = new Compressor(RobotMap.PneumaticsCan0);
    }

    public void initDefaultCommand() {
    }

    public void enableCompressor() {
        compressor.setClosedLoopControl(true);
    }

    public void disableCompressor() {
        compressor.setClosedLoopControl(false);
    }
}
