package frc.robot.controllers;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.CargoPickupMode;
import frc.robot.commands.HatchPickupMode;
import frc.robot.commands.autonomous.VisionMode;
import frc.robot.commands.drivetrain.SetSlowMode;
import frc.robot.commands.intake.intake.CalibrateIntakeEncoder;
import frc.robot.commands.intake.intake.SetIntakePosition;
import frc.robot.commands.intake.piston.QuickPistonAndVacuum;
import frc.robot.commands.intake.vacuum.SetVacuum;
import frc.robot.commands.lift.CalibrateLiftEncoder;
import frc.robot.commands.lift.SetLiftPosition;
import frc.robot.commands.teleop.DriverMode;
import frc.robot.map.RobotMap;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.Drivetrain.SlowModes;
import frc.robot.util.Deadband;

public class Attack3 extends OIBase {
    static Deadband deadband = new Deadband(0, 0.15);

    public Attack3() {
        super();
    }

    public Attack3(Integer port) {
        super(port);
    }

    int getDefaultPort() {
        return RobotMap.Joystick.attack3_0Port();
    }

    void setUpButtons() {
        getButton(1).whenPressed(new QuickPistonAndVacuum());

        getButton(3).whenPressed(new SetVacuum(true));
        getButton(2).whenPressed(new SetVacuum(false));

        getButton(7).whenPressed(new SetSlowMode(SlowModes.Slow));
        getButton(7).whenReleased(new SetSlowMode(SlowModes.Normal));
        
        getButton(9).whenPressed(new CalibrateIntakeEncoder());
        getButton(8).whenPressed(new CalibrateLiftEncoder());
        
        getButton(10).whenPressed(new HatchPickupMode());
        getButton(10).whenPressed(new CargoPickupMode());
    }

    public double getLeftSpeed() {
        return -deadband.calc(getJoystick().getRawAxis(1), RobotMap.Joystick.squareThrottle());
    }

    public double getLeftRotation() {
        return deadband.calc(getJoystick().getRawAxis(0), RobotMap.Joystick.squareTurn());
    }

    public double getRightSpeed() {
        return -deadband.calc(getJoystick().getRawAxis(2)); // Mini slider at bottom
    }

    public boolean getSlowMode() {
        return getButton(2).get();
    }
}
