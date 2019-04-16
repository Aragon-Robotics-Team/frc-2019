package frc.robot.vision;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionThread;

public class Comms {
    public static TimeServices timeServices;

    public static void createVisionThread(VideoSource camera, CvSource outputVideo,
            BiConsumer<GripPostProcessing, CvSource> callback) {

        System.out.println("init visionthread");
        GripPostProcessing pipeline = new GripPostProcessing();
        VisionThread visionThread = new VisionThread(camera, pipeline, (p) -> callback.accept(p, outputVideo));
        /*
         * something like this for GRIP: VisionThread visionThread = new
         * VisionThread(cameras.get(0), new GripPipeline(), pipeline -> { ...
         * });CvSource
         */
        visionThread.start();
        System.out.println("started visionthread");
    }

    public static void gripProcessVideo(GripPostProcessing p, CvSource outputVideo) {
        // outputVideo.putFrame(p.AugmentCamOutput);
        double[] x_offset_angles = new double[p.visionTargets.size()];
        for (int i = 0; i < p.visionTargets.size(); i++) {
            GripPostProcessing.VisionTarget v = p.visionTargets.get(i);
            x_offset_angles[i] = CoordTransform
                    .transformCoordsToOffsetAngle(new double[] { (double) v.bounding.x + 0.5 * v.bounding.width,
                            (double) v.bounding.y + 0.5 * v.bounding.height })[0];
        }
        ByteArrayOutput.setNetworkObject(x_offset_angles, "table", "target_offsets");
        if (p.visionTargets.size() > 0) {
            NetworkTableInstance inst = NetworkTableInstance.getDefault();
            edu.wpi.first.networktables.NetworkTable table = inst.getTable("table");
            NetworkTableEntry entry = table.getEntry("target_sighted");
            entry.setBoolean(true);
        } else {
            NetworkTableInstance inst = NetworkTableInstance.getDefault();
            edu.wpi.first.networktables.NetworkTable table = inst.getTable("table");
            NetworkTableEntry entry = table.getEntry("target_sighted");
            entry.setBoolean(false);
        }

        Duration latency;
        synchronized (Comms.timeServices.ping) {
            latency = Comms.timeServices.ping;
        }

        latency = latency.plus(Duration.between(p.t, Comms.timeServices.clock.instant()));
        ByteArrayOutput.setNetworkObject(latency, "table", "latency");

    }

    public static void gripProcessVideoDiagnostic(GripPostProcessing p, CvSource outputVideo) {
        outputVideo.putFrame(p.AugmentCamOutput);
        double[] x_offset_angles = new double[p.visionTargets.size()];
        for (int i = 0; i < p.visionTargets.size(); i++) {
            GripPostProcessing.VisionTarget v = p.visionTargets.get(i);
            x_offset_angles[i] = CoordTransform
                    .transformCoordsToOffsetAngle(new double[] { (double) v.bounding.x + 0.5 * v.bounding.width,
                            (double) v.bounding.y + 0.5 * v.bounding.height })[0];
        }
        ByteArrayOutput.setNetworkObject(x_offset_angles, "table", "target_offsets");
        if (p.visionTargets.size() > 0) {
            NetworkTableInstance inst = NetworkTableInstance.getDefault();
            edu.wpi.first.networktables.NetworkTable table = inst.getTable("table");
            NetworkTableEntry entry = table.getEntry("target_sighted");
            entry.setBoolean(true);
        } else {
            NetworkTableInstance inst = NetworkTableInstance.getDefault();
            edu.wpi.first.networktables.NetworkTable table = inst.getTable("table");
            NetworkTableEntry entry = table.getEntry("target_sighted");
            entry.setBoolean(false);
        }
        Duration latency;
        synchronized (Comms.timeServices.ping) {
            latency = Comms.timeServices.ping;
        }

        latency = latency.plus(Duration.between(p.t, Comms.timeServices.clock.instant()));
        ByteArrayOutput.setNetworkObject(latency, "table", "latency");
    }

    public static void initTimeServices() {
        timeServices = new TimeServices();
    }

    public static class TimeServices {
        public Clock clock = Clock.systemUTC();
        public Timer time = new Timer();
        public Duration ping = Duration.ZERO;
        public Instant ping_instant;
        public Pinger pinger;

        public TimeServices() {

        }

        public void start() {
            this.pinger = new Pinger();
        }

        public class Pinger extends TimerTask {
            public NetworkTableEntry ping_service = NetworkTableInstance.getDefault().getTable("table")
                    .getEntry("ping_service");

            public Pinger() {
                ping_service.addListener((event) -> {
                    if (event.value.getString().equals("pong")) {
                        synchronized (ping) {
                            ping = Duration.between(ping_instant, clock.instant());
                        }
                    }
                }, EntryListenerFlags.kUpdate | EntryListenerFlags.kLocal);
                time.schedule(this, 0, 70);
            }

            public void run() {
                ping_instant = clock.instant();
                ping_service.setString("ping");
            }
        }
    }
}
