package frc.robot.controllers;

import frc.robot.commands.intake.hatch.SetHatch;
import frc.robot.commands.intake.piston.QuickPistonAndVacuum;
import frc.robot.commands.intake.vacuum.HoldVacuumOn;
import frc.robot.map.RobotMap;
import frc.robot.util.Deadband;

public class F310 extends OIBase {
    static Deadband deadband = new Deadband(0, 0.2);
    static Deadband newDeadband = new Deadband(0, 0.1);
    
    public F310() {
        super();
    }

    public F310(Integer port) {
        super(port);
    }

    int getDefaultPort() {
        return RobotMap.Joystick.f310Port();
    }

    void setUpButtons() {
        getButton(1).whenPressed(new SetHatch(true));
        getButton(1).whenReleased(new SetHatch(false));
        getButton(2).whenPressed(new QuickPistonAndVacuum());

        getButton(3).toggleWhenActive(new HoldVacuumOn());
    }
    
    public double getLeftSpeed() {
        return -1.0 * newDeadband.calc(getJoystick().getRawAxis(1));
    }

    public double getLeftRotation() {
        return newDeadband.calc(getJoystick().getRawAxis(4));
    }

    public double getRightSpeed() {
        return -1.0 * deadband.calc(getJoystick().getRawAxis(5), true);
    }

    public boolean getSlowMode() {
        return getButton(6).get();
    }
}
