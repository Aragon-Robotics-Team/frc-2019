package frc.robot.util;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Robot;

public class BetterSolenoid implements BetterSendable {
    Solenoid solenoid;
    boolean isReal;

    static Deadband deadband = new Deadband(0.25, 0);

    public BetterSolenoid(Integer port) {
        var map = Robot.map.pneumatics;

        isReal = port != null;
        solenoid = isReal ? new Solenoid(map.PCMCanID(), port) : Mock.mock(Solenoid.class);
    }

    public void createSendable(SendableMaster master) {
        master.add(solenoid);
    }

    public void set(boolean on) {
        solenoid.set(on);
    }

    public boolean get() {
        return solenoid.get();
    }
}
