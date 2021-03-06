package frc.robot.controllers;

import frc.robot.commands.intake.intake.StowIntakeAlways;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Lift;

// Left
public class ButtonBoard2 extends OIBase {
    public ButtonBoard2() {
        super();
    }

    public ButtonBoard2(Integer port) {
        super(port);
    }

    int getDefaultPort() {
        return RobotMap.Joystick.bb2Port();
    }

    void setUpButtons() {
        getButton(1).whenPressed(new StowIntakeAlways(false));
        getButton(2).whenPressed(new StowIntakeAlways(true));

        getButton(3).whenPressed(new SetLiftPosition(Lift.Position.Hatch1));
        getButton(4).whenPressed(new SetLiftPosition(Lift.Position.Hatch2));
        // getButton(5).whenPressed(new SetLiftPosition(Lift.Position.Hatch3));

        // getButton(6).whenPressed(new SetLiftPosition(Lift.Position.Port3));
        getButton(6).whenPressed(new SetLiftPosition(Lift.Position.CargoPort));
        getButton(7).whenPressed(new SetLiftPosition(Lift.Position.Port2));
        getButton(8).whenPressed(new SetLiftPosition(Lift.Position.Port1));

        // 12: Emergency stop (not connected?)
    }
}
