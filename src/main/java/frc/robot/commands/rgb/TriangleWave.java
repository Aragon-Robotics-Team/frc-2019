package frc.robot.commands.rgb;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class TriangleWave extends Command {
    double period;

    public TriangleWave(double period) {
        this.period = period;
    }

    public void execute() {
        Robot.myRGB.setBrightness((-Timer.getFPGATimestamp() % period) / period);
    }

    public boolean isFinished() {
        return false;
    }
}
