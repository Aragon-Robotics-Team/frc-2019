package frc.robot.util;

import static org.mockito.Mockito.mock;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.RobotMap;

public class BetterSolenoid extends SendableBase {
    Solenoid solenoid;
    boolean on;

    public BetterSolenoid(int id, boolean isInstalled) {
        solenoid = (RobotMap.PNEUMATICS_CONTROL_MODULE_INSTALLED && isInstalled)
                ? (new Solenoid(RobotMap.PNEUMATICS_CONTROL_MODULE_CAN, id))
                : mock(Solenoid.class);

        set(false);
    }

    public BetterSolenoid(int id) {
        this(id, true);
    }

    public void set(boolean on) {
        solenoid.set(on);
        this.on = on;
    }

    public boolean get() {
        return on;
    }

    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Solenoid");
        builder.addBooleanProperty("Value", this::get, this::set);
    }
}
