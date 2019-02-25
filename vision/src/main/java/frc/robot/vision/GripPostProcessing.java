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
import java.util.stream.Stream;
import java.util.Arrays;
import frc.robot.vision.CoordTransform;

public class GripPostProcessing implements VisionPipeline {
    public GripInterface grip;
    public Mat AugmentCamOutput = new Mat();
    public RotatedRect[] rects;

    public List<MatOfPoint> filtered_contours;
    public ArrayList<VisionTarget> visionTargets;

    public GripPostProcessing() {
        super();
        this.grip = new Grip();
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

    }

    public void process(Mat source0) {
        grip.process(source0);
        filtered_contours = new ArrayList<MatOfPoint>();
        rects = filterMinAreaRects(getMinAreaRects(grip), grip.filterContoursOutput(), 0.77,
                filtered_contours);
        visionTargets = getVisionTargets(rects);



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
            Imgproc.putText(AugmentCamOutput, String.format("%.1f", correct_angle(rects[i])),
                    rectCorners[3], 0, 0.4, new Scalar(255, 255, 255));
            Imgproc.putText(AugmentCamOutput,
                    String.format("%.2f", rectangularity(rects[i], filtered_contours.get(i))),
                    rectCorners[1], 0, 0.4, new Scalar(0, 255, 255));
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
            Point[] p1 = new Point[4];
            Point[] p2 = new Point[4];
            MatOfPoint p = new MatOfPoint();
            r1.points(p1);
            r2.points(p2);
            p1 = Stream.concat(Arrays.stream(p1), Arrays.stream(p2)).toArray(Point[]::new); // thnx
                                                                                            // stackoverflow
            p.fromArray(p1);
            this.bounding = Imgproc.boundingRect(p);
        }
    }

    public ArrayList<VisionTarget> getVisionTargets(RotatedRect[] rects) {
        // RotatedRect[] rects = getMinAreaRects(grip); // fills rects array using contours

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

    public double rectangularity(RotatedRect rect, MatOfPoint contour) {
        return Math.abs(Imgproc.contourArea(contour) / rect.size.area());
    }

    public boolean isTarget(RotatedRect rect1, RotatedRect rect2) {
        double angleDiff = Math.abs(correct_angle(rect1) - correct_angle(rect2));
        double horizon = ((correct_angle(rect1) + correct_angle(rect2)) / 2) + 90;
        Point rect1_center = new Point(CoordTransform
                .rotate(new double[] {rect1.center.x, rect1.center.y}, -horizon * Math.PI / 180));
        Point rect2_center = new Point(CoordTransform
                .rotate(new double[] {rect2.center.x, rect2.center.y}, -horizon * Math.PI / 180));


        double width1 = rect1.size.width;
        double width2 = rect2.size.width;
        double height1 = rect1.size.height;
        double height2 = rect2.size.height;
        if (width1 > height1) {
            double temp = height1;
            height1 = width1;
            width1 = temp;
        }
        if (width2 > height2) {
            double temp = height2;
            height2 = width2;
            width2 = temp;
        }

        double avgWidth = (width1 + width2) / 2;

        double avgHeight = (height1 + height2) / 2;

        if (18 < angleDiff && angleDiff < 38
                && Math.abs(rect1.size.area()
                        - rect2.size.area()) < (rect1.size.area() + rect2.size.area()) * .25
                && Math.abs(rect1_center.x - rect2_center.x) < 3 * (avgHeight) // if distance x <
                                                                               // 6*height
                && Math.abs(rect1_center.y - rect2_center.y) < avgHeight // if distance y < height
        )
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

    public RotatedRect[] filterMinAreaRects(RotatedRect[] rects, List<MatOfPoint> Contours,
            double threshold) {
        // thresholds by rectangularity(rotatedrect, contour) -> value from 0-1
        ArrayList<RotatedRect> pass_rects = new ArrayList<RotatedRect>();
        for (int i = 0; i < rects.length; i++) {
            if (rectangularity(rects[i], Contours.get(i)) > threshold) {
                pass_rects.add(rects[i]);
            }
        }
        RotatedRect[] pass_rects_arr = new RotatedRect[pass_rects.size()];
        pass_rects_arr = pass_rects.toArray(pass_rects_arr);
        return pass_rects_arr;
    }

    public RotatedRect[] filterMinAreaRects(RotatedRect[] rects, List<MatOfPoint> contours,
            double threshold, List<MatOfPoint> filtered_contours) {
        // thresholds by rectangularity(rotatedrect, contour) -> value from 0-1
        ArrayList<RotatedRect> pass_rects = new ArrayList<RotatedRect>();
        for (int i = 0; i < rects.length; i++) {
            if (rectangularity(rects[i], contours.get(i)) > threshold) {
                pass_rects.add(rects[i]);
                filtered_contours.add(contours.get(i));
            }
        }
        RotatedRect[] pass_rects_arr = new RotatedRect[pass_rects.size()];
        pass_rects_arr = pass_rects.toArray(pass_rects_arr);
        return pass_rects_arr;
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
        // returns angle, 0-180 (horizontal, from +x axis counterclockwise to +y axis to -x axis ,
        // along longest side)
        if (rect.size.width < rect.size.height) {
            return -rect.angle + 90;
        } else {
            return -rect.angle;
        }

    }

}
