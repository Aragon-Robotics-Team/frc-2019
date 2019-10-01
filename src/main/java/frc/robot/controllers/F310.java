package frc.robot.controllers;

import frc.robot.commands.intake.hatch.SetHatch;
import frc.robot.commands.intake.piston.QuickPiston;
import frc.robot.commands.intake.vacuum.HoldVacuumOn;
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
        getButton(1).whenPressed(new SetHatch(true));
        getButton(1).whenReleased(new SetHatch(false));
        getButton(2).whenPressed(new QuickPiston());

        getButton(3).toggleWhenActive(new HoldVacuumOn());
    }
}
