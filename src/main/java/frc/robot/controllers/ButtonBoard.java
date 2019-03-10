package frc.robot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.Robot;
import frc.robot.commands.intake.SetIntakePosition;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;

public class ButtonBoard {
    Joystick joystick;
    Button[] buttons;

    public ButtonBoard() {
        joystick = new Joystick(Robot.map.joystick.buttonBoardPort());

        final int AMOUNT_BUTTONS = 10;

        for (int index = 1; index < (AMOUNT_BUTTONS + 1); index++) {
            buttons[index] = new JoystickButton(joystick, index);
        }

        buttons[0].whenPressed(new SetIntakePosition(Intake.Position.Horizontal));
        buttons[1].whenPressed(new SetIntakePosition(Intake.Position.Intake));
        buttons[2].whenPressed(new SetIntakePosition(Intake.Position.Vertical));
        buttons[3].whenPressed(new SetIntakePosition(Intake.Position.Stowed));

        buttons[4].whenPressed(new SetLiftPosition(Lift.Position.Port1));
        buttons[5].whenPressed(new SetLiftPosition(Lift.Position.Port2));
        buttons[6].whenPressed(new SetLiftPosition(Lift.Position.Port3));

        buttons[7].whenPressed(new SetLiftPosition(Lift.Position.Hatch1));
        buttons[8].whenPressed(new SetLiftPosition(Lift.Position.Hatch2));
        buttons[9].whenPressed(new SetLiftPosition(Lift.Position.Hatch3));
    }
}
