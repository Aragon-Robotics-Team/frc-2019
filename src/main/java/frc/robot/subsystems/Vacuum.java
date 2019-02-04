package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.RobotMap;

public class Vacuum extends Subsystem {
    Relay vacuumController;

    public Vacuum() {
        // Direction is either kForward or kReverse
        vacuumController = new Relay(RobotMap.VacuumRelay, Relay.Direction.kForward);
        off();

        Shuffleboard.getTab("Drive").add(vacuumController);
    }

    public void on() {
        vacuumController.set(Relay.Value.kOn);
    }

    public void off() {
        vacuumController.set(Relay.Value.kOff);
    }

    public void set(boolean isOn) {
        if (isOn) {
            on();
        } else {
            off();
        }
    }

    public void initDefaultCommand() {
    }
}
