package frc.robot.controllers;

import frc.robot.commands.intake.QuickPiston;
import frc.robot.commands.intake.SetIntakePosition;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Intake;

public class ButtonBoard1 extends OIBase {
    int getDefaultPort() {
        return RobotMap.Joystick.bb1Port();
    }

    void setUpButtons() {
        getButton(1).whenPressed(new QuickPiston());

        getButton(2).whenPressed(new SetIntakePosition(Intake.Position.Stowed));
        getButton(3).whenPressed(new SetIntakePosition(Intake.Position.Vertical));
        getButton(4).whenPressed(new SetIntakePosition(Intake.Position.Intake));
        getButton(5).whenPressed(new SetIntakePosition(Intake.Position.Horizontal));

        // 6 - 9 : Hab mechanism
        // 12 : Emergency stop
    }
}
