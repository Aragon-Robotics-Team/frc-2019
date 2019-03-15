package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetBrakeMode extends Command {
    boolean brake;

    public SetBrakeMode(boolean brake) {
        setRunWhenDisabled(true);

        this.brake = brake;
    }

    public void initialize() {
        Robot.myDrivetrain.setBrake(brake);
    }

    public boolean isFinished() {
        return true;
    }
}
