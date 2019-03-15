package frc.robot.controllers;

import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;

public class ButtonBoardOld extends OIBase {
    public ButtonBoardOld() {
        super();
    }

    public ButtonBoardOld(Integer port) {
        super(port);
    }

    int getDefaultPort() {
        return RobotMap.Joystick.bbOldPort();
    }

    void setUpButtons() {
        getButton(1).whenPressed(new SetIntakePosition(Intake.Position.Stowed));
        getButton(2).whenPressed(new SetIntakePosition(Intake.Position.Vertical));
        getButton(3).whenPressed(new SetIntakePosition(Intake.Position.Intake));
        getButton(4).whenPressed(new SetIntakePosition(Intake.Position.Horizontal));

        getButton(5).whenPressed(new SetLiftPosition(Lift.Position.Port1));
        getButton(6).whenPressed(new SetLiftPosition(Lift.Position.Port2));
        getButton(7).whenPressed(new SetLiftPosition(Lift.Position.Port3));

        getButton(8).whenPressed(new SetLiftPosition(Lift.Position.Hatch1));
        getButton(9).whenPressed(new SetLiftPosition(Lift.Position.Hatch2));
        getButton(10).whenPressed(new SetLiftPosition(Lift.Position.Hatch3));
    }
}
