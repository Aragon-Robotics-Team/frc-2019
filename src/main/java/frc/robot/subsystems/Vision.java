package frc.robot.subsystems;

import java.time.Clock;
import java.time.Instant;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.util.BetterSendable;
import frc.robot.util.Mock;
import frc.robot.util.SendableMaster;

public class Vision extends Subsystem implements BetterSendable {
    public Instant pi_instant;
    public Instant rio_instant;
    public Clock clock = Clock.systemUTC();
    Relay ledController;

    public Vision() {
        var map = Robot.map.vision;

        ledController = Mock.createMockable(Relay.class, map.ledPort());
        ledController.setDirection(Relay.Direction.kForward);
        ledController.setSafetyEnabled(false);

        setLeds(false);
    }

    public void createSendable(SendableMaster master) {
        var map = Robot.map.vision;

        if (map.ledPort() != null) { // Sigh... I just can't get rid of this
            master.add(ledController);
        }
    }

    public void setLeds(boolean on) {
        if (on) {
            ledController.set(Relay.Value.kOn);
            ledController.set(Relay.Value.kOff);
        }
    }

    public void initDefaultCommand() {
    }

    public class time_listener implements TableEntryListener {
        public Vision parent;

        public time_listener(Vision parent) {
            this.parent = parent;
        }

        public void valueChanged(NetworkTable table, String key, NetworkTableEntry entry,
                NetworkTableValue value, int flags) {
            ByteArrayInput.deserialize(this.parent.pi_instant, value.getRaw());
            this.parent.rio_instant = this.parent.clock.instant();
        }
    }
}
