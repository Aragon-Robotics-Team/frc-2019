package frc.robot.util;

import frc.robot.util.BetterFollower.FollowerController;

public class BetterFollowerConfig {
    public boolean invert;
    public BetterFollower.FollowerController controller;
    public boolean isConnected;

    public BetterFollowerConfig() {
        invert = false;
        controller = FollowerController.VictorSPX;
        isConnected = true;
    }
}
