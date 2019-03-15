package frc.robot.map;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

class DrivetrainImpl extends RobotMap.Drivetrain {
    public Integer leftMainCanID() {
        return null;
    }

    public Integer rightMainCanID() {
        return null;
    }

    public Integer leftSlaveCanID() {
        return null;
    }

    public Integer rightSlaveCanID() {
        return null;
    }

    public boolean invertLeft() {
        return false;
    }

    public boolean invertLeftEncoder() {
        return false;

    }

    public boolean invertRight() {
        return false;

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
        return null;
    }

    public Integer vacuumPort() {
        return null;
    }


    public Integer pistonPCMPort() {
        return null;
    }

    public boolean invertIntake() {
        return false;
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
        return null;
    }

    public boolean invertLift() {
        return false;
    }

    public boolean invertLiftEncoder() {
        return false;
    }
}


class PneumaticsImpl extends RobotMap.Pneumatics {
    public Integer PCMCanID() {
        return null;
    }
}


class VisionImpl extends RobotMap.Vision {
    public Integer ledPort() {
        return null;
    }
}


public class NullRobotMap extends RobotMap {
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
