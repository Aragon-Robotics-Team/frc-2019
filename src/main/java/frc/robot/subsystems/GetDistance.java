package frc.robot.subsystems;

import frc.robot.subsystems.Drivetrain;

public class GetDistance {
    public double changeX;
    public double changeY;
    public double changeAngle;
    public double initialAngle = 0;
    public double finalAngle = 0;

    public GetDistance(double timeChange) {
        double rightSpeed = Drivetrain.leftPos;
        double leftSpeed = Drivetrain.rightPos;


        double length = 18; // length from the center of the robot to the center

        double movement = .5 * (rightSpeed + leftSpeed);
        double rotation = .5 * (rightSpeed - leftSpeed);

        changeAngle = (rotation / length) * timeChange;
        finalAngle = initialAngle + changeAngle;

        if (changeAngle < 0.001) {
            changeX = movement * timeChange * Math.cos(Math.toRadians(finalAngle));
            changeY = movement * timeChange * Math.sin(Math.toRadians(finalAngle));

        } else {
            changeX = (length * movement) / rotation * (Math.sin(Math.toRadians(finalAngle))
                    - Math.sin(Math.toRadians(initialAngle)));
            changeY = (length * movement) / rotation * (Math.cos(Math.toRadians(finalAngle))
                    - Math.cos(Math.toRadians(initialAngle)));
        }
    }
}
