package frc.robot.commands.rgb;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class StrobeSquareWave extends Command {
    double period;
    double pulseCycle;

    public StrobeSquareWave(double period, double pulseCycle) {
        this.period = period;
        this.pulseCycle = pulseCycle;
    }

    public void execute() {
        Robot.myRGB.setOn(((Timer.getFPGATimestamp() % period) / period) < pulseCycle);
    }

    public boolean isFinished() {
        return false;
    }
}
