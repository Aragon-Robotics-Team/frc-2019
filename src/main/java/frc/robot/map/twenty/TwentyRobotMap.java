package frc.robot.map.twenty;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.robot.map.RobotMap;

class DrivetrainImpl extends RobotMap.Drivetrain {
    public Integer leftMainCanID() {
        return 1;
    }

    public Integer rightMainCanID() {
        return 4;
    }

    public Integer leftSlaveCanID() {
        return 6;
    }

    public Integer rightSlaveCanID() {
        return 5;
    }

    public boolean invertLeft() {
        // unconfirmed
        return false;
    }

    public boolean invertLeftEncoder() {
        return false;
    }

    public boolean invertRight() {
        // unconfirmed
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
        return 2;
    }

    public Integer vacuumPort() {
        return 4;
    }

    public Integer pistonPCMPort() {
        return 0;
    }
    
    public Integer hatchPCMPort() {
        return 1;
    }

    public boolean invertIntake() {
        return true;
    }

    public boolean invertIntakeEncoder() {
        return false;
    }

    public boolean invertVacuum() {
        return true;
    }

}


class LiftImpl extends RobotMap.Lift {
    public Integer controllerCanID() {
        return 5;
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


public class TwentyRobotMap extends RobotMap {
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
        return false;
    }
}
