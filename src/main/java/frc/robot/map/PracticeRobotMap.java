package frc.robot.map;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

class DrivetrainImpl extends RobotMap.Drivetrain {
    public Integer leftMainCanID() {
        return 5;
    }

    public Integer rightMainCanID() {
        return 3;
    }

    public Integer leftSlaveCanID() {
        return 6;
    }

    public Integer rightSlaveCanID() {
        return 3;
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
}


class LiftImpl extends RobotMap.Lift {
    public Integer controllerCanID() {
        return null;
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


public class PracticeRobotMap extends RobotMap {
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
