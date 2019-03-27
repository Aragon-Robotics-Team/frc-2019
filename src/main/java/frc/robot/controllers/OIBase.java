package frc.robot.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger;

public abstract class OIBase extends SendableBase implements OI {
    private Joystick joystick;
    private Button[] buttons;
    private POVTrigger povTrigger;

    private static final int AMOUNT_BUTTONS = 12;

    public OIBase() {
        this(null);
    }

    public OIBase(Integer port) {
        if (port == null) {
            port = getDefaultPort();
        }

        joystick = new Joystick(port);
        buttons = new Button[AMOUNT_BUTTONS + 1]; // So buttons range 1-max; button0 = null

        for (int index = 1; index < (AMOUNT_BUTTONS + 1); index++) {
            buttons[index] = new JoystickButton(joystick, index);
        }

        povTrigger = new POVTrigger(this);

        setUpButtons();
    }

    abstract int getDefaultPort();

    abstract void setUpButtons();

    final Joystick getJoystick() {
        return joystick;
    }

    final POVTrigger getPOVTrigger() {
        return povTrigger;
    }

    final Button getButton(int button) {
        return buttons[button];
    }

    public double getLeftSpeed() {
        return 0;
    }

    public double getLeftRotation() {
        return 0;
    }

    public double getRightSpeed() {
        return 0;
    }

    public boolean getSlowMode() {
        return false;
    }

    public Integer getPOV() {
        int pov = getJoystick().getPOV();
        switch (pov) { // -180 to 180
            case 0:
                return 3;
            case 45:
                return 4;
            case 90:
                return 5;
            case 135:
                return 6;
            case 180:
                return 7;
            case 225:
                return 0;
            case 270:
                return 1;
            case 315:
                return 2;
        }
        return null;
    }
}


class POVTrigger extends Trigger {
    public OIBase oi;

    public POVTrigger(OIBase oi) {
        this.oi = oi;
    }

    public boolean get() {
        return (oi.getPOV() != null);
    }
}
