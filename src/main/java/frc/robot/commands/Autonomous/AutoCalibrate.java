package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.NavX;
import java.time.*;

public class AutoCalibrate extends Command {
    public int stage;
    public double p;
    public double i;
    public double d;
    public double amplitude;
    public double error;
    public double target;
    public Instant startTime;

    public Clock clock;

    public AutoCalibrate(double target) {
        requires(Robot.myNavX);
        requires(Robot.myDrivetrain);
        this.target = target;
    }

    protected void initialize() {
        startTime = clock.instant();
    }

    protected void execute() {

    }

    public boolean isFinished() {
        // placeholder
        return true;
    }
}
