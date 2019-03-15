package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.util.BetterSpeedController;

public abstract class ControlMotorJoystick extends Command {
    BetterSpeedController controller;
    double max;

    public ControlMotorJoystick(BetterSpeedController controller) {
        super();
        this.controller = controller;
        requires(Robot.map.oiSubsystem);
    }

    protected double getValue() {
        return Robot.map.oi.getRightSpeed();
    }

    protected double getMax() {
        return 1;
    }

    protected void execute() {
        controller.set(getValue() * getMax());
    }

    protected boolean isFinished() {
        return false;
    }
}
