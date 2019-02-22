package frc.robot.util;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

public class BetterTalonSRXConfig extends TalonSRXConfiguration {
    public boolean invert;
    public boolean invertEncoder;
    public double ticksPerInch;
    public NeutralMode neutralMode;
    public Deadband deadband;
    public boolean isConnected;

    public BetterTalonSRXConfig() {
        ticksPerInch = 0;
        invert = false;
        invertEncoder = false;
        neutralMode = NeutralMode.Brake;
        deadband = new Deadband(0.125, 0); // Warning: 0 deadband!;
        isConnected = true;

        // Set defaults below for non-BetterTalonSRXConfig options

        openloopRamp = 0.1;
        nominalOutputForward = 0.1;
        nominalOutputReverse = 0.1;
        clearPositionOnLimitR = true;
        voltageCompSaturation = 10.0;

        // Todo: current limit, voltage compensation
    }
}
