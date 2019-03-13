package frc.robot.subsystems;

import java.time.Duration;
import java.util.ArrayList;
import org.opencv.core.Point;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.subsystems.Vision.VisionPositioningServices.PoseHistory.Pose;
import frc.robot.util.BetterSendable;
import frc.robot.util.Mock;
import frc.robot.util.SendableMaster;
import frc.robot.util.fieldmap.MapInference;

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
        public PoseHistory poseHistory = new PoseHistory(200);

        public static class PoseHistory {
            public static class Pose {
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
                synchronized (concretes) {
                    synchronized (Robot.myDrivetrain.syncLock) {
                        for (int i = 0; i < history_length; i++) {
                            concretes.add(new Pose(
                                    new Point(Robot.myDrivetrain.x, Robot.myDrivetrain.y)));
                        }
                    }
                }
            }

            public void addPoseToHistory(Pose p) {
                synchronized (concretes) {
                    concretes.add(p);
                    concretes.remove(concretes.size() - 1);
                }
            }

            public Point getPos(double time) {
                int l = 0;
                int h = concretes.size() - 1;
                int m;
                while (l <= h) {
                    m = (l + h) / 2;
                    if (concretes.get(m).time < time) {
                        l = m + 1;
                    } else {
                        h = m - 1;
                    }
                }
                if (l > concretes.size() - 1) {
                    return concretes.get(concretes.size() - 1).position;
                }
                if (h < 0) {
                    return concretes.get(0).position;
                }
                Pose hPose, lPose;
                synchronized (concretes) {
                    hPose = concretes.get(h);
                    lPose = concretes.get(l);
                }
                double time_delta = hPose.time - lPose.time;
                double time_position = time - hPose.time;
                double x_delta = hPose.position.x - lPose.position.x;
                double y_delta = hPose.position.y - lPose.position.y;
                double x_position = hPose.position.x + x_delta * (time_position / time_delta);
                double y_position = hPose.position.y + y_delta * (time_position / time_delta);
                return (new Point(x_position, y_position));
            }

            public void UpdatePoseHistory(Pose p, double trust) {
                Point oldPos = getPos(p.time);
                Point delta = new Point((p.position.x - oldPos.x) * trust,
                        (p.position.y - oldPos.y) * trust);
                synchronized (concretes) {
                    for (int i = 0; i < concretes.size(); i++) {
                        concretes.get(i).position.x += delta.x;
                        concretes.get(i).position.y += delta.y;
                    }
                }
                synchronized (Robot.myDrivetrain.syncLock) {
                    Robot.myDrivetrain.x += delta.x;
                    Robot.myDrivetrain.y += delta.y;
                }
            }
        }

        public VisionPositioningServices() {
            NetworkTableInstance inst = NetworkTableInstance.getDefault();
            NetworkTable table = inst.getTable("table");
            NetworkTableEntry latency = table.getEntry("latency");
            latency.addListener((event) -> {
                Duration latency_val =
                        ByteArrayInput.getNetworkObject(Duration.ZERO, "table", "latency");
                double[] angles =
                        ByteArrayInput.getNetworkObject(new double[0], "table", "target_offsets");
                if (angles.length != 0) {
                    Point avgNewPos = new Point(0, 0);
                    double map_angle;
                    Point oldPos = poseHistory.getPos((double) latency_val.getSeconds()
                            + latency_val.getNano() / 1000000000 - Timer.getFPGATimestamp());
                    Point newPos;
                    for (double angle : angles) {
                        map_angle = Robot.myNavX.ahrs.getAngle() + 90 - angle;
                        newPos = MapInference.getPos(oldPos, map_angle,
                                MapInference.get_closest_targets_by_angle(oldPos, map_angle)[0]);
                        avgNewPos.x += newPos.x;
                        avgNewPos.y += newPos.y;
                    }
                    avgNewPos.x /= angles.length;
                    avgNewPos.y /= angles.length;
                    poseHistory.UpdatePoseHistory(new Pose(avgNewPos,
                            (double) latency_val.getSeconds() + latency_val.getNano() / 1000000000),
                            0.1);

                } else {
                    System.out.println(
                            "latentcy.getDouble(-1) returned -1... This is not supposed to happen!!");
                }
            }, EntryListenerFlags.kUpdate | EntryListenerFlags.kLocal);
        }
    }

}
