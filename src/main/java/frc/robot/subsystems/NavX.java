package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.util.BetterSendable;
import frc.robot.util.Mock;
import frc.robot.util.SendableMaster;

public class NavX extends Subsystem implements BetterSendable {
    public AHRS ahrs;
    boolean isReal;
    NavXSendable sendable;

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

        sendable = new NavXSendable(this);
    }

    public String getTabName() {
        return "Angle";
    }

    public void createSendable(SendableMaster master) {
        master.add(sendable);
        master.add("Yaw", ahrs);
        master.add(new InstantCommand("Reset Yaw", this::zeroYaw));
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


class NavXSendable extends SendableBase {
    NavX navx;

    public NavXSendable(NavX navx) {
        this.navx = navx;
    }

    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("NavX Angle", navx.ahrs::getYaw, null);
        builder.addBooleanProperty("NavX Enabled", navx::isRunning, null);
    }
}
