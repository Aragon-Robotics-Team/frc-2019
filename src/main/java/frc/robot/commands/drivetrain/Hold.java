package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Hold extends Command {
    double l;
    double r;

    public Hold() {
        requires(Robot.myDrivetrain);
    }

    public void initialize() {
        l = Robot.myDrivetrain.leftController.get();
        r = Robot.myDrivetrain.rightController.get();
    }

    public void execute() {
        Robot.myDrivetrain.control(l, r);
    }

    public boolean isFinished() {
        return false;
    }
}
