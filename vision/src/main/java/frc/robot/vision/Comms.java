package frc.robot.vision;

import java.util.function.BiConsumer;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.vision.VisionThread;

public class Comms {
    public static void createVisionThread(VideoSource camera, CvSource outputVideo,
            BiConsumer<GripPostProcessing, CvSource> callback) {
        GripPostProcessing pipeline = new GripPostProcessing();
        VisionThread visionThread =
                new VisionThread(camera, pipeline, (p) -> callback.accept(p, outputVideo));
        /*
         * something like this for GRIP: VisionThread visionThread = new
         * VisionThread(cameras.get(0), new GripPipeline(), pipeline -> { ... });CvSource
         */
        System.out.println("start visionthread");
        visionThread.start();
    }

    public static void gripProcessVideo(GripPostProcessing p, CvSource outputVideo) {
        double[] x_offset_angles = new double[p.visionTargets.size()];
        for (int i = 0; i < p.visionTargets.size(); i++) {
            GripPostProcessing.VisionTarget v = p.visionTargets.get(i);
            x_offset_angles[i] = CoordTransform.transformCoordsToOffsetAngle(
                    new double[] {(double) v.bounding.x + 0.5 * v.bounding.width,
                            (double) v.bounding.y + 0.5 * v.bounding.height})[0];
        }
        ByteArrayOutput.setNetworkObject(p.t, "table", "timestamp");
        ByteArrayOutput.setNetworkObject(x_offset_angles, "table", "target_offsets");
    }
}
