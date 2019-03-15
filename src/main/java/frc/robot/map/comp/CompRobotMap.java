package frc.robot.map.comp;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.robot.map.RobotMap;

class DrivetrainImpl extends RobotMap.Drivetrain {
    public Integer leftMainCanID() {
        return 7;
    }

    public Integer rightMainCanID() {
        return 8;
    }

    public Integer leftSlaveCanID() {
        return 1;
    }

    public Integer rightSlaveCanID() {
        return 2;
    }

    public boolean invertLeft() {
        return false;
    }

    public boolean invertLeftEncoder() {
        return false;

    }

    public boolean invertRight() {
        return true;

    }

    public boolean invertRightEncoder() {
        return false;

    }

    public Class<? extends BaseMotorController> slaveController() {
        return VictorSPX.class;
    }
}


class IntakeImpl extends RobotMap.Intake {
    public Integer controllerCanID() {
        return 4;
    }

    public Integer vacuumPort() {
        return 0;
    }

    public Integer pistonPCMPort() {
        return 0;
    }

    public boolean invertIntake() {
        return true;
    }

    public boolean invertIntakeEncoder() {
        return false;
    }

    public boolean invertVacuum() {
        return false;
    }

}


class LiftImpl extends RobotMap.Lift {
    public Integer controllerCanID() {
        return 1;
    }

    public boolean invertLift() {
        return true;
    }

    public boolean invertLiftEncoder() {
        return false;
    }
}


class PneumaticsImpl extends RobotMap.Pneumatics {
    public Integer PCMCanID() {
        return 2;
    }
}


class VisionImpl extends RobotMap.Vision {
    public Integer ledPort() {
        return null;
    }
}


public class CompRobotMap extends RobotMap {
    public Drivetrain getDrivetrain() {
        return new DrivetrainImpl();
    }

    public Intake getIntake() {
        return new IntakeImpl();
    }

    public Lift getLift() {
        return new LiftImpl();
    }

    public Pneumatics getPneumatics() {
        return new PneumaticsImpl();
    }

    public Vision getVision() {
        return new VisionImpl();
    }

    public boolean navXInstalled() {
        return true;
    }
}
