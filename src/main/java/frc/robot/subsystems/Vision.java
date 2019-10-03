package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.robot.Robot;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.GyroSendable;
import frc.robot.util.Mock;
import frc.robot.util.SendableMaster;

public class Vision extends BetterSubsystem implements BetterSendable {
    Relay ledController;

    public boolean seeTarget;
    public double[] targetPos = new double[2];
    public double calculatedYaw;

    static NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("table");
    static NetworkTableEntry positionEntry = networkTable.getEntry("target_offsets");
    static NetworkTableEntry existsEntry = networkTable.getEntry("target_sighted");

    static final int imgWidth = 320;
    static final int imgHeight = 240;
    static final int aspectH = 16;
    static final int aspectV = 9;
    static final double fovD = Math.toRadians(68.5);

    static final double centerH = (imgWidth / 2.0) + 0.5;
    // static final double centerV = (imgHeight / 2.0) + 0.5;

    static final double aspectD = Math.hypot(aspectH, aspectV);
    static final double fovH = Math.atan(Math.tan(fovD / 2.0) * (aspectH / aspectD)) * 2.0;
    // static double fovV = Math.atan(Math.tan(fovD / 2.0) * (aspectV / aspectD)) * 2.0;

    static final double focalH = imgWidth / (2.0 * Math.tan(fovH / 2.0));
    // static final double focalV = imgWidth / (2.0 * Math.tan(fovV / 2.0));

    static final double calculateYaw(double pixelH) {
        return Math.toDegrees(Math.atan2(pixelH - centerH, focalH));
    }

    public Vision() {
        var map = Robot.map.vision;

        ledController = Mock.createMockable(Relay.class, map.ledPort());
        ledController.setDirection(Relay.Direction.kForward);
        ledController.setSafetyEnabled(false);

        setLeds(false);
        
        CameraServer.getInstance().startAutomaticCapture("Sandstorm", 0);
    }

    public void createSendable(SendableMaster master) {
        master.add(new SendableVision(this));
        master.add("Yaw", new GyroSendable(() -> calculatedYaw));
        var map = Robot.map.vision;

        if (map.ledPort() != null) { // Sigh... I just can't get rid of this
            master.add(ledController);
        }
    }

    public void periodic() {
        updateTarget();
    }

    public void updateTarget() {
        targetPos = positionEntry.getDoubleArray(new double[2]);
        seeTarget = existsEntry.getBoolean(false);
        calculatedYaw = seeTarget ? calculateYaw(targetPos[0]) : 0.0;
    }

    public void setLeds(boolean on) {
        if (on) {
            ledController.set(Relay.Value.kOn);
        } else {
            ledController.set(Relay.Value.kOff);
        }
    }
}


class SendableVision extends SendableBase {
    Vision vision;

    public SendableVision(Vision vision) {
        this.vision = vision;
    }

    public void initSendable(SendableBuilder builder) {
        // builder.addBooleanProperty("Target", () -> vision.seeTarget, null);
        builder.addDoubleProperty("X", () -> vision.targetPos[0], null);
        // builder.addDoubleProperty("Y", () -> vision.targetPos[1], null);
    }
}
