package frc.robot.subsystems;

import static frc.robot.util.Mock.mock;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Pneumatics extends Subsystem {
    Compressor compressor;
    boolean enabled;

    public Pneumatics() {
        compressor = RobotMap.PNEUMATICS_CONTROL_MODULE_INSTALLED
                ? new Compressor(RobotMap.PNEUMATICS_CONTROL_MODULE_CAN)
                : mock(Compressor.class);
    }

    public void initDefaultCommand() {
    }

    public void setCompressor(boolean enabled) {
        System.out.println("Compressor: " + enabled);
        compressor.setClosedLoopControl(enabled);
        this.enabled = enabled;
    }

    public boolean compressorStatus() {
        return enabled;
    }

    public double compressorCurrent() {
        return compressor.getCompressorCurrent();
    }

    public boolean pressureSwitchStatus() {
        return compressor.getPressureSwitchValue();
    }
}
