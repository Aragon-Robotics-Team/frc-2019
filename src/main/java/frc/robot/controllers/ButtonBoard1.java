package frc.robot.controllers;

import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;

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
        getButton(2).whenPressed(new SetIntakePosition(Intake.Position.Stowed));
        getButton(3).whenPressed(new SetIntakePosition(Intake.Position.Vertical));
        getButton(4).whenPressed(new SetIntakePosition(Intake.Position.Intake));
        getButton(5).whenPressed(new SetIntakePosition(Intake.Position.Horizontal));


        getButton(6).whenPressed(new SetLiftPosition(Lift.Position.Stowed));
        getButton(7).whenPressed(new SetLiftPosition(Lift.Position.Port1));
        getButton(8).whenPressed(new SetLiftPosition(Lift.Position.CargoPort));
        getButton(9).whenPressed(new SetLiftPosition(Lift.Position.Hatch2));
        getButton(1).whenPressed(new SetLiftPosition(Lift.Position.Port2));
        
        // 6 - 9 : Hab mechanism
        // 12 : Emergency stop
    }
}
