package frc.robot.controllers;

import frc.robot.commands.angle.TurnAbsoluteCardinalAndDrive;
import frc.robot.commands.intake.piston.QuickPiston;
import frc.robot.commands.intake.vacuum.SetVacuum;
import frc.robot.map.RobotMap;
import frc.robot.util.Deadband;

public class HotasX extends OIBase {
    static Deadband deadband = new Deadband(0, 0);

    public HotasX() {
        super();
    }

    public HotasX(Integer port) {
        super(port);
    }

    int getDefaultPort() {
        return RobotMap.Joystick.hotasPort();
    }

    void setUpButtons() {
        getButton(2).whenPressed(new SetVacuum(true));
        getButton(3).whenPressed(new SetVacuum(false));
        getButton(4).whenPressed(new SetVacuum(false));

        getButton(6).whenPressed(new QuickPiston());
        getButton(9).whenPressed(new QuickPiston());
        getPOVTrigger().whenActive(new TurnAbsoluteCardinalAndDrive());

        // 10 : turn to visible target

        // POV
        // left: left 90deg
        // right: right 90deg
        // up/down: turn 180deg
    }

    public double getLeftSpeed() {
        return -deadband.calc(getJoystick().getRawAxis(2), RobotMap.Joystick.squareThrottle());
    }

    public double getLeftRotation() {
        return deadband.calc(getJoystick().getRawAxis(0), RobotMap.Joystick.squareTurn());
    }

    public double getRightSpeed() {
        // Slider on left hand - right is positive
        return deadband.calc(getJoystick().getRawAxis(4), true);
    }

    public boolean getSlowMode() {
        return getButton(1).get() || disableCompressor();
    }

    public boolean disableCompressor() {
        return getButton(3).get();
    }
}
