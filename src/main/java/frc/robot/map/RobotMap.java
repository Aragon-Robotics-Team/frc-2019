package frc.robot.map;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

import frc.robot.controllers.Attack3;
import frc.robot.controllers.GuestController;
import frc.robot.controllers.NullOI;
import frc.robot.controllers.OI;
import frc.robot.map.comp.CompRobotMap;
import frc.robot.util.BetterSendable;
import frc.robot.util.BetterSubsystem;
import frc.robot.util.SendableMaster;

public abstract class RobotMap implements BetterSendable {
    public static RobotMap getMap() {
        return new CompRobotMap();
    }

    public OI oi;
    public OI guestOI;
    public BetterSubsystem oiSubsystem = new BetterSubsystem();

    public void init() {
        if (oi == null) {
            // oi = new MultiOI(new HotasX(), new ButtonBoard1(), new ButtonBoard2());
            // oi = new F310();
            // oi = new NullOI();
            oi = new Attack3(1);

            guestOI = new GuestController(4);
        }
    }

    public static class Joystick {
        public static int hotasPort() {
            return 0;
        }

        // Right
        public static int bb1Port() {
            return 2;
        }

        // Left
        public static int bb2Port() {
            return 3;
        }

        public static int attack3_0Port() {
            return 1;
        }

        public static int attack3_1Port() {
            return 4;
        }

        public static int f310Port() {
            return 0;
        }

        public static int bbOldPort() {
            return 5;
        }

        public static boolean squareThrottle() {
            return true;
        }

        public static boolean squareTurn() {
            return true;
        }
    }

    public Drivetrain drivetrain = getDrivetrain();

    public abstract Drivetrain getDrivetrain();

    public static abstract class Drivetrain {
        public abstract Integer leftMainCanID();

        public abstract Integer rightMainCanID();

        public abstract Integer leftSlaveCanID();

        public abstract Integer rightSlaveCanID();

        public abstract boolean invertLeft();

        public abstract boolean invertLeftEncoder();

        public abstract boolean invertRight();

        public abstract boolean invertRightEncoder();

        public abstract Class<? extends BaseMotorController> slaveController();
    }

    public Intake intake = getIntake();

    public abstract Intake getIntake();

    public static abstract class Intake {
        public abstract Integer controllerCanID();

        public abstract Integer vacuumPort();

        public abstract Integer pistonPCMPort();

        public abstract boolean invertIntake();

        public abstract boolean invertIntakeEncoder();

        public abstract boolean invertVacuum();
    }

    public Lift lift = getLift();

    public abstract Lift getLift();

    public static abstract class Lift {
        public abstract Integer controllerCanID();

        public abstract boolean invertLift();

        public abstract boolean invertLiftEncoder();
    }

    public Pneumatics pneumatics = getPneumatics();

    public abstract Pneumatics getPneumatics();

    public static abstract class Pneumatics {

        public abstract Integer PCMCanID();
    }

    public Vision vision = getVision();

    public abstract Vision getVision();

    public static abstract class Vision {
        public abstract Integer ledPort();
    }

    public abstract boolean navXInstalled();

    public String getTabName() {
        // return oi.getTabName();
        return "Drivetrain";
    }

    public void createSendable(SendableMaster master) {
        init();
        master.add("OI", (BetterSendable) oi);
        master.add("Guest OI", (BetterSendable) guestOI);
    }
}
