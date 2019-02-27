package frc.robot.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;

public class BetterFollower<T extends BaseMotorController> {
    IMotorController master;
    T slave;
    boolean invert;

    public BetterFollower(T slave, IMotorController master, boolean invert) {
        this.master = master;
        this.slave = slave;
        this.invert = invert;

        slave.follow(master, FollowerType.PercentOutput);
        slave.setInverted(calcInvertType(invert));
    }

    public BetterFollower(Class<T> clazz, int canId, IMotorController master, boolean invert) {
        this(initFollowerFromClass(clazz, canId), master, invert);
    }

    public static InvertType calcInvertType(boolean invert) {
        // if invert == false: FollowMaster
        // if invert == true: OpposeMaster
        return (invert ? InvertType.OpposeMaster : InvertType.FollowMaster);
    }

    public static <T extends BaseMotorController> T initFollowerFromClass(
            Class<? extends BaseMotorController> clazz, int canId) {
        try {
            Constructor<? extends BaseMotorController> constructor =
                    clazz.getConstructor(Integer.class);
            return constructor.newInstance(canId);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException // wtf java
                | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
