package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class SetOpenloopRamp extends Command {

    public SetOpenloopRamp() {
        // requires(Robot.myDrivetrain);
    }

    protected void initialize() {
        Robot.myDrivetrain.setOpenloopRamp(Robot.myDrivetrain.rampSet.getNumber(0).doubleValue());
    }

    protected void execute() {

    }

    protected boolean isFinished() {
        return true;
    }
}
