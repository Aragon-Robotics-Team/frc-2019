package frc.robot.controllers;

import frc.robot.commands.autonomous.AutoAlign;
import frc.robot.commands.drivetrain.ControlArcadeDrivetrain;
import frc.robot.map.RobotMap;
import frc.robot.util.Deadband;

public class F310 extends OIBase {
    static Deadband deadband = new Deadband(0, 0.2);

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
        getButton(1).whenPressed(new ControlArcadeDrivetrain());
        getButton(2).whileActive(new AutoAlign());
        getButton(2).whenReleased(new ControlArcadeDrivetrain());
    }

    public double getLeftSpeed() {
        return -1.0 * getJoystick().getRawAxis(1);
    }

    public double getLeftRotation() {
        return getJoystick().getRawAxis(4);
    }

    public double getRightSpeed() {
        return -1.0 * deadband.calc(getJoystick().getRawAxis(5), true);
    }

    public boolean getSlowMode() {
        return getButton(6).get();
    }
}
