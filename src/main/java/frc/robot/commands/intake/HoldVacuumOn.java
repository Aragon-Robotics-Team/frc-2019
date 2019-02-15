package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class HoldVacuumOn extends Command {

    public HoldVacuumOn() {
    }

    protected void initialize() {
        Robot.myIntake.setVacuum(true);
    }

    protected void execute() {
    }

    protected void end() {
        Robot.myIntake.setVacuum(false);
    }

    protected boolean isFinished() {
        return false;
    }
}
