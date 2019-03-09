package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.util.Mock;

public class Pneumatics extends Subsystem {
    Compressor compressor;
    boolean enabled;

    public Pneumatics() {
        var map = Robot.map.pneumatics;

        compressor = Mock.createMockable(Compressor.class, map.PCMCanID());
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
