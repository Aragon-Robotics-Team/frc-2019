package frc.robot.util;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class BetterFollowerConfig {
    public boolean invert;
    public Class<? extends BaseMotorController> controller;

    public BetterFollowerConfig() {
        invert = false;
        controller = VictorSPX.class;
    }
}
