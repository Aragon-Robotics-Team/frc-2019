package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Sol0On extends Command {

    public Sol0On() {
        requires(Robot.myPneumatics);
    }

    protected void initialize() {
        Robot.myPneumatics.sol0Set(true);
    }

    protected void execute() {
    }

    protected void interrupted() {
        end();
    }

    protected void end() {
        Robot.myPneumatics.sol0Set(false);
    }

    protected boolean isFinished() {
        return false;
    }
}
