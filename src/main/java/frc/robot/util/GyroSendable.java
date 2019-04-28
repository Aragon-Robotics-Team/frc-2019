package frc.robot.util;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class GyroSendable extends SendableBase {
    DoubleSupplier supplier;

    public GyroSendable(DoubleSupplier supplier) {
        this.supplier = supplier;
    }

    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", supplier, null);
    }
}
