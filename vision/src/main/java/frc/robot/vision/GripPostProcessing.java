package frc.robot.vision;

import frc.robot.vision.Grip;
import edu.wpi.first.vision.VisionPipeline;
import org.opencv.core.Mat;
import org.opencv.imgproc.*;
import org.opencv.core.*;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;
import frc.robot.vision.CoordTransform;

public class GripPostProcessing implements VisionPipeline {
    public GripInterface grip;
    public Mat AugmentCamOutput = new Mat();
    public RotatedRect[] rects;
    public ArrayList<VisionTarget> visionTargets = new ArrayList<VisionTarget>();

    public GripPostProcessing() {
        super();
        this.grip = new Grip();
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

    }

    public void process(Mat source0) {
        grip.process(source0);
        visionTargets = getVisionTargets(grip);

        rects = getMinAreaRects(grip);

        // System.out.println(source0.cols() + " " + source0.rows()); //prints size of camera input
        source0.copyTo(AugmentCamOutput);

        for (int i = 0; i < grip.filterContoursOutput().size(); i++) {
            // step draw contours
            Imgproc.drawContours(AugmentCamOutput, grip.filterContoursOutput(), i,
                    new Scalar(255, 0, 0), 1);
        }
        Point[] rectCorners = new Point[4];
        MatOfPoint p = new MatOfPoint();
        List<MatOfPoint> plist = new ArrayList<MatOfPoint>();
        for (int i = 0; i < rects.length; i++) {
            rects[i].points(rectCorners);
            p.fromArray(rectCorners);
            plist.add(p);
            Imgproc.drawContours(AugmentCamOutput, plist, i, new Scalar(0, 0, 255), 1);
            // Imgproc.putText(AugmentCamOutput, "" + correct_angle(rects[i]), rectCorners[0], 0, 8,
            // new Scalar(0, 255, 255));
        }

        // step draw rectangles around visiontargets
        for (int i = 0; i < visionTargets.size(); i++) {
            Imgproc.rectangle(AugmentCamOutput, visionTargets.get(i).bounding.tl(),
                    visionTargets.get(i).bounding.br(), new Scalar(255, 128, 0), 1);
        }

    }

    public class VisionTarget implements Serializable {
        public double x;
        public double y;
        public Rect bounding;

        public VisionTarget(RotatedRect r1, RotatedRect r2) {
            // shit.. well:
            // r1 is implied to be the left rectangle
            // r2 is the right rectangle
            this.x = (r1.center.x + r2.center.x) / 2;
            this.y = (r1.center.y + r2.center.y) / 2;
            this.bounding = new Rect(r1.boundingRect().tl(), r2.boundingRect().br());
        }
    }

    public ArrayList<VisionTarget> getVisionTargets(GripInterface grips) {
        RotatedRect[] rects = getMinAreaRects(grip); // fills rects array using contours

        // in-place. This is to identify pairs. not the perfect solution
        rects = sortRectsByX(rects);
        ArrayList<VisionTarget> visionTargets = new ArrayList<VisionTarget>();

        for (int i = 0; i < rects.length - 1; i++) {
            if (isTarget(rects[i], rects[i + 1])) {
                visionTargets.add(new VisionTarget(rects[i], rects[i + 1]));
            }
        }

        return visionTargets;
    }

    public boolean isTarget(RotatedRect rect1, RotatedRect rect2) {
        double angleDiff = Math.abs(correct_angle(rect1) - correct_angle(rect2));
        double vertical = angleDiff;
        double horizon = angleDiff;
        double[] rect1_coords =
                CoordTransform.rotate(new double[] {rect1.center.x, rect1.center.y}, horizon);
        if (angleDiff < 80 && angleDiff > 5
                && Math.abs(rect1.center.x - rect2.center.x) < 3
                        * (rect1.size.height + rect2.size.height)
                && Math.abs(
                        rect1.center.y - rect2.center.y) < (rect1.size.height + rect2.size.height)
                                / 2)
            return true;
        else
            return false;
    }

    public RotatedRect[] getMinAreaRects(GripInterface grip) {
        rects = new RotatedRect[grip.filterContoursOutput().size()];
        for (int i = 0; i < grip.filterContoursOutput().size(); i++) {
            rects[i] = Imgproc
                    .minAreaRect(new MatOfPoint2f(grip.filterContoursOutput().get(i).toArray()));
        }
        return rects;
    }

    class RectComparator implements Comparator<RotatedRect> {
        public int compare(RotatedRect a, RotatedRect b) {
            return (int) (a.center.x - b.center.x);
        }
    }

    public RotatedRect[] sortRectsByX(RotatedRect[] rects) {
        // assuming this is smallest to largest for now, check RectComparator to fix
        Arrays.sort(rects, new RectComparator());
        return rects;
    }

    public double correct_angle(RotatedRect rect) {
        // seems to output angles as -90 to 90, zero being vertical?
        // don't touch... voodoo math inside
        double angle;
        if (rect.size.width < rect.size.height) {
            angle = -1 * (rect.angle - 90);
        } else {
            angle = rect.angle;
        }

        if (angle > 90) {
            angle = -1 * (angle - 180);
        }
        // this is... I'm too lazy to reverse engineer this out of the code, but it's
        // known to
        // work
        angle *= -1;

        return angle;
    }
}
