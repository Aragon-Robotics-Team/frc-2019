package frc.robot.subsystems;

import static org.mockito.Mockito.mock;
import java.time.Clock;
import java.time.Instant;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.RobotMap;

public class Vision extends Subsystem {
    public Instant pi_instant;
    public Instant rio_instant;
    public Clock clock;

    Relay ledController;

    ShuffleboardTab tab;

    public Vision() {
        ledController =
                RobotMap.VISION_LED_RELAY_INSTALLED ? (new Relay(RobotMap.VISION_LED_RELAY_PORT))
                        : mock(Relay.class);
        ledController.setDirection(Relay.Direction.kForward);
        ledController.setSafetyEnabled(false);

        tab = Shuffleboard.getTab("Vision");
        tab.add(ledController);

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

    public class init_time_listener implements TableEntryListener {
        public Vision parent;

        public init_time_listener(Vision parent) {
            this.parent = parent;
        }

        public void valueChanged(NetworkTable table, String key, NetworkTableEntry entry,
                NetworkTableValue value, int flags) {
            ByteArrayInput.deserialize(this.parent.pi_instant, value.getRaw());
            this.parent.rio_instant = this.parent.clock.instant();
        }
    }
}
