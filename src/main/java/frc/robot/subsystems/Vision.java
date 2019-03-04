package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;
import frc.robot.util.Mock;

public class Vision extends Subsystem {
    Relay ledController;

    ShuffleboardTab tab;

    public Vision() {
        var map = Robot.map.vision;

        ledController = Mock.createMockable(Relay.class, map.ledPort());
        ledController.setDirection(Relay.Direction.kForward);
        ledController.setSafetyEnabled(false);

        tab = Shuffleboard.getTab("Vision");
        if (map.ledPort() != null) { // Sigh... I just can't get rid of this
            tab.add(ledController);
        }

        setLeds(false);
    }

    public void setLeds(boolean on) {
        if (on) {
            ledController.set(Relay.Value.kOn);
        } else {
            ledController.set(Relay.Value.kOff);
        }
    }

    public void initDefaultCommand() {

    }
}
