package frc.robot.util;

import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public class BetterFollower {
    IMotorController master;
    BaseMotorController slave;
    boolean invert;

    public BetterFollower(Integer canID, BetterFollowerConfig config) {
        master = null;
        invert = config.invert;

        slave = Mock.createMockable(config.controller, canID);
        slave.setInverted(calcInvertType(invert));
    }

    void follow(IMotorController master) {
        slave.follow(master, FollowerType.PercentOutput);
        this.master = master;
    }

    void setBrakeMode(NeutralMode neutralMode) {
        slave.setNeutralMode(neutralMode);
    }

    public static InvertType calcInvertType(boolean invert) {
        // if invert == false: FollowMaster
        // if invert == true: OpposeMaster
        return (invert ? InvertType.OpposeMaster : InvertType.FollowMaster);
    }
}
