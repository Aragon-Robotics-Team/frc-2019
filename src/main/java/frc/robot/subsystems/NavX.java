package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;

public class NavX extends Subsystem {
    public AHRS ahrs;

    public NavX() {
        // Choosing interface:
        // https://pdocs.kauailabs.com/navx-mxp/guidance/selecting-an-interface/

        try {
            ahrs = new AHRS(SPI.Port.kMXP);
        } catch (RuntimeException ex) {
            DriverStation.reportError("Can't start NavX", true);
        }
    }

    public void initDefaultCommand() {
    }

    public boolean isRunning() {
        if (ahrs == null) {
            return false;
        }

        return !(ahrs.isCalibrating());
    }

    public void zeroYaw() {
        if (ahrs == null) {
            return;
        }

        ahrs.reset(); // Might be either reset() or zeroYaw()
    }
}
