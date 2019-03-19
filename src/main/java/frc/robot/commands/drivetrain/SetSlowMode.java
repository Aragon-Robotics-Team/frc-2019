package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain.SlowModes;

public class SetSlowMode extends Command {
    SlowModes slowMode;

    public SetSlowMode(SlowModes slowMode) {
        setRunWhenDisabled(true);
        this.slowMode = slowMode;
    }

    protected void initialize() {
        Robot.myDrivetrain.setSlow(slowMode);
    }

    protected boolean isFinished() {
        return true;
    }
}
