package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class HoldVacuumOn extends Command {

    public HoldVacuumOn() {
    }

    protected void initialize() {
        Robot.myIntake.on();
    }

    protected void execute() {
    }

    protected void end() {
        Robot.myIntake.off();
    }

    protected boolean isFinished() {
        return false;
    }
}
