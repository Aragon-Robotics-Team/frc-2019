package frc.robot.map;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import frc.robot.controllers.OI;
import frc.robot.controllers.SplitArcadeAttack3;
import frc.robot.util.BetterSendable;
import frc.robot.util.SendableMaster;

public abstract class RobotMap implements BetterSendable {
    public static RobotMap getMap() {
        return new PracticeRobotMap();
    }

    public Joystick joystick = getJoystick();

    public OI oi = new SplitArcadeAttack3(joystick.attack3_0Port(), joystick.attack3_1Port());

    Joystick getJoystick() {
        return new Joystick();
    }

    public static class Joystick {
        int f310Port() {
            return 0;
        }

        int attack3_0Port() {
            return 1;
        }

        int attack3_1Port() {
            return 4;
        }

        public int buttonBoardPort() {
            return 5;
        }

        public boolean squareThrottle() {
            return true;
        }

        public boolean squareTurn() {
            return true;
        }
    }

    public Drivetrain drivetrain = getDrivetrain();

    abstract Drivetrain getDrivetrain();

    public static abstract class Drivetrain {
        public abstract Integer leftMainCanID();

        public abstract Integer rightMainCanID();

        public abstract Integer leftSlaveCanID();

        public abstract Integer rightSlaveCanID();

        public abstract Class<? extends BaseMotorController> slaveController();
    }

    public Intake intake = getIntake();

    abstract Intake getIntake();

    public static abstract class Intake {
        public abstract Integer controllerCanID();

        public abstract Integer vacuumPort();

        public abstract Integer pistonPCMPort();
    }

    public Lift lift = getLift();

    abstract Lift getLift();

    public static abstract class Lift {
        public abstract Integer controllerCanID();
    }

    public Pneumatics pneumatics = getPneumatics();

    abstract Pneumatics getPneumatics();

    public static abstract class Pneumatics {

        public abstract Integer PCMCanID();
    }

    public Vision vision = getVision();

    abstract Vision getVision();

    public static abstract class Vision {
        public abstract Integer ledPort();
    }

    public abstract boolean navXInstalled();

    public String getTabName() {
        return oi.getTabName();
    }

    public void createSendable(SendableMaster master) {
        master.add((BetterSendable) oi);
    }
}
