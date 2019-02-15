package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Pneumatics extends Subsystem {
    Compressor compressor;

    public Pneumatics() {
        compressor = new Compressor(RobotMap.PNEUMATICS_CONTROL_MODULE_CAN);
    }

    public void initDefaultCommand() {
    }

    public void enableCompressor() {
        compressor.setClosedLoopControl(true);
    }

    public void disableCompressor() {
        compressor.setClosedLoopControl(false);
    }

    public boolean compressorStatus() {
        boolean enabled = compressor.enabled();
        return enabled;
    }

    public double compressorCurrent() {
        double current = compressor.getCompressorCurrent();
        return current;
    }

    public boolean pressureSwitchStatus() {
        boolean pressureSwitch = compressor.getPressureSwitchValue();
        return pressureSwitch;
    }
}
