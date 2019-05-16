package frc.robot.controllers;

import edu.wpi.first.wpilibj.command.PrintCommand;
import frc.robot.commands.intake.piston.QuickPistonAndVacuum;
import frc.robot.commands.intake.vacuum.SetVacuum;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Lift;

public class GuestController extends Attack3 {
    public GuestController() {
        super();
    }

    public GuestController(Integer port) {
        super(port);
    }

    int getDefaultPort() {
        return RobotMap.Joystick.attack3_1Port();
    }

    void setUpButtons() {
        getButton(3).whenPressed(new PrintCommand("Guest 3"));
        getButton(6).whenPressed(new SetLiftPosition(Lift.Position.CargoPort));
        getButton(7).whenPressed(new SetLiftPosition(Lift.Position.Stowed));
        getButton(2).whenPressed(new SetVacuum(true));
        getButton(1).whenPressed(new QuickPistonAndVacuum());
    }
}
