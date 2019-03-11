package frc.robot.subsystems;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.opencv.core.Point;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.util.BetterSendable;
import frc.robot.util.Mock;
import frc.robot.util.SendableMaster;

public class Vision extends Subsystem implements BetterSendable {
    Relay ledController;
    public TimeServices timeServices = new TimeServices();
    public VisionPositioningServices visionPositioningServices = new VisionPositioningServices();

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

    public class TimeServices {
        public TimeServices() {
            NetworkTableInstance inst = NetworkTableInstance.getDefault();
            NetworkTable table = inst.getTable("table");
            NetworkTableEntry ping_service = table.getEntry("ping_service");
            ping_service.addListener((event) -> {
                if (event.value.getString().equals("ping")) {
                    ping_service.setString("pong");
                }
            }, EntryListenerFlags.kUpdate | EntryListenerFlags.kLocal);
        }
    }

    public static class VisionPositioningServices {
        public PoseHistory poseHistory = new PoseHistory(20);

        public class PoseHistory {
            public class Pose {
                public Point position;
                public double time;

                public Pose(Point position, double time) {
                    this.position = position;
                    this.time = time;
                }

                public Pose(Point position) {
                    this.position = position;
                    this.time = Timer.getFPGATimestamp();
                }
            }

            public ArrayList<Pose> concretes = new ArrayList<Pose>();
            public int history_length;

            public PoseHistory(int history_length) {
                this.history_length = history_length;
                for (int i = 0; i < history_length; i++) {
                    synchronized (concretes) {
                        synchronized (Robot.myDrivetrain.syncLock) {
                            concretes.add(new Pose(
                                    new Point(Robot.myDrivetrain.x, Robot.myDrivetrain.y)));
                        }
                    }
                }
            }

            public void addPoseToHistory(Pose p) {
                concretes.add(p);
                concretes.remove(concretes.size() - 1);
            }
        }

        public VisionPositioningServices() {

        }
    }

}
