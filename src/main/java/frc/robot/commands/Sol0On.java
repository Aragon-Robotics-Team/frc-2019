package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class Sol0On extends Command {

    public Sol0On() {
        System.out.println("IIInit");
        requires(Robot.myPneumatics);
    }

    protected void initialize() {
        System.out.println("initial");
        Robot.myPneumatics.sol0Set(true);
    }

    protected void execute() {
        // System.out.println("exec");
        // Robot.myPneumatics.sol0Set(true);
    }

    protected void interrupted() {
        System.out.println("inter");
        end();
    }

    protected void end() {
        System.out.println("end");
        Robot.myPneumatics.sol0Set(false);
    }

    protected boolean isFinished() {
        return false; // Run always
    }
}
