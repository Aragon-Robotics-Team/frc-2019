package frc.robot.commands.rgb;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class HueCycle extends Command {
    double period;

    public HueCycle(double period) {
        requires(Robot.myRGB);
        this.period = period;
    }

    public void initialize() {
        Robot.myRGB.setOn(true);
    }

    public void execute() {
        Robot.myRGB.setHSV(Timer.getFPGATimestamp() / period);
    }

    public void end() {
        Robot.myRGB.setOn(false);
    }

    public boolean isFinished() {
        return false;
    }
}
