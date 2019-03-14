package frc.robot.controllers;

import frc.robot.commands.intake.intake.CalibrateIntakeEncoder;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.intake.piston.QuickPiston;
import frc.robot.commands.lift.CalibrateLiftEncoder;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Intake;

// Right
public class ButtonBoard1 extends OIBase {
    public ButtonBoard1() {
        super();
    }

    public ButtonBoard1(Integer port) {
        super(port);
    }

    int getDefaultPort() {
        return RobotMap.Joystick.bb1Port();
    }

    void setUpButtons() {
        getButton(1).whenPressed(new QuickPiston());

        getButton(2).whenPressed(new SetIntakePosition(Intake.Position.Stowed));
        getButton(3).whenPressed(new SetIntakePosition(Intake.Position.Vertical));
        getButton(4).whenPressed(new SetIntakePosition(Intake.Position.Intake));
        getButton(5).whenPressed(new SetIntakePosition(Intake.Position.Horizontal));

        getButton(6).whenPressed(new CalibrateIntakeEncoder());
        getButton(7).whenPressed(new CalibrateLiftEncoder());

        // 6 - 9 : Hab mechanism
        // 12 : Emergency stop
    }
}
