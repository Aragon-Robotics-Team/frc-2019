package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import frc.robot.Robot;
import frc.robot.util.Disableable;
import frc.robot.util.Mock;

public class Pneumatics implements Disableable {
    Compressor compressor;
    boolean enabled;

    public Pneumatics() {
        var map = Robot.map.pneumatics;

        compressor = Mock.createMockable(Compressor.class, map.PCMCanID());
    }

    public void setCompressor(boolean enabled) {
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

    public void disable() {
        setCompressor(false);
    }

    public void enable() {
        setCompressor(true);
    }
}
