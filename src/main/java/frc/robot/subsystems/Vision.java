package frc.robot.subsystems;

import static org.mockito.Mockito.mock;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

public class Vision extends Subsystem {
    Relay ledController;

    public Vision() {
        ledController =
                RobotMap.VISION_LED_RELAY_INSTALLED ? (new Relay(RobotMap.VISION_LED_RELAY_PORT))
                        : mock(Relay.class);
        ledController.setDirection(Relay.Direction.kForward);
        ledController.setSafetyEnabled(false);

        setLeds(false);
    }

    public void setLeds(boolean on) {
        if (on) {
            ledController.set(Relay.Value.kOn);
        } else {
            ledController.set(Relay.Value.kOff);
        }
    }

    public void initDefaultCommand() {

    }
}
