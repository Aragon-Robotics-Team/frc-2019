package frc.robot.vision;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

public interface GripInterface {
    public List<MatOfPoint> filterContoursOutput();

    public void process(Mat source0);
}
