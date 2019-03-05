package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.Robot;
import frc.robot.util.Mock;

public class NavX extends Subsystem {
    public AHRS ahrs;
    boolean isReal;

    public NavX() {
        var map = Robot.map;
        isReal = map.navXInstalled();

        // Choosing interface:
        // https://pdocs.kauailabs.com/navx-mxp/guidance/selecting-an-interface/

        try {
            ahrs = Mock.createMockable(AHRS.class, SPI.Port.kMXP, isReal);
        } catch (RuntimeException ex) {
            DriverStation.reportError("Can't start NavX", true);
        }

        var tab = Shuffleboard.getTab("Gyro");
        tab.add(new InstantCommand("Reset Yaw", this::zeroYaw));
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
