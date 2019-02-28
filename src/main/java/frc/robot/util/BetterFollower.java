package frc.robot.util;

import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class BetterFollower {
    IMotorController master;
    BaseMotorController slave;
    boolean invert;

    public enum FollowerController {
        TalonSRX, VictorSPX;
    }

    public BetterFollower(FollowerController controller, int canID, boolean invert) {
        slave = createController(controller, canID);
        slave.setInverted(calcInvertType(invert));

        master = null;
        this.invert = invert;
    }

    void follow(IMotorController master) {
        slave.follow(master, FollowerType.PercentOutput);
        this.master = master;
    }

    void setBrakeMode(NeutralMode neutralMode) {
        slave.setNeutralMode(neutralMode);
    }

    public static BaseMotorController createController(FollowerController controller, int canID) {
        BaseMotorController slave;

        switch (controller) {
            case TalonSRX:
                slave = new TalonSRX(canID);
                break;
            case VictorSPX:
                slave = new VictorSPX(canID);
                break;
            default:
                throw new IllegalArgumentException();
        }

        return slave;

    }

    public static InvertType calcInvertType(boolean invert) {
        // if invert == false: FollowMaster
        // if invert == true: OpposeMaster
        return (invert ? InvertType.OpposeMaster : InvertType.FollowMaster);
    }
}
