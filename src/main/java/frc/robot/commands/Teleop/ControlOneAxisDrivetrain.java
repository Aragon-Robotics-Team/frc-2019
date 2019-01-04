package frc.robot.commands.Teleop;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ControlOneAxisDrivetrain extends Command {
    double lspeed;
    double rspeed;
    final double speed;
    final static double timeout = 0.1;
    final static double correction = 0.2;

    public ControlOneAxisDrivetrain(double speed) {
        requires(Robot.myDrivetrain);
        this.speed = speed;
    }

    protected void initialize() {
        Robot.myDrivetrain.resetDistance();
        setTimeout(timeout); // sec to update
        this.lspeed = speed;
        this.rspeed = speed;
    }

    protected void execute() {
        double leftSpeed = Robot.m_oi.getLeftSpeed() * lspeed;
        double rightSpeed = Robot.m_oi.getLeftSpeed() * rspeed;

        Robot.myDrivetrain.control(leftSpeed, rightSpeed);

        if (isTimedOut()) {
            double rDistance = Robot.myDrivetrain.getRawRightDistance();
            double lDistance = Robot.myDrivetrain.getRawLeftDistance();
            double error = rDistance - lDistance;
            Robot.myDrivetrain.resetDistance();
            lspeed += error * correction * Math.signum(rDistance); // correction for sign

            lspeed = Math.max(Math.min(lspeed, 1.0), 0.0);

            System.out.println(
                    String.format("timeout: control %.3f rD %.3f lD %.3f left %.3f right %.3f",
                            Robot.m_oi.getLeftSpeed(), lDistance, rDistance, lspeed, rspeed));

            setTimeout(timeout);
        }
    }

    protected boolean isFinished() {
        return false; // Run forever
        // return true; // is broken, end now
    }
}
