package frc.robot.util;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.RobotMap;

public class BetterSolenoid extends SendableBase {
    Solenoid solenoid;

    public BetterSolenoid(int id) {
        solenoid = new Solenoid(RobotMap.PNEUMATICS_CONTROL_MODULE_CAN, id);
    }

    public void set(boolean on) {
        solenoid.set(on);
    }

    public boolean get() {
        return solenoid.get();
    }

    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Solenoid");
        builder.addBooleanProperty("Value", this::get, this::set);
    }
}
