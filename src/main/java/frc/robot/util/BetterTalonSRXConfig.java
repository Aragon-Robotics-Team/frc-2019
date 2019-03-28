package frc.robot.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

public class BetterTalonSRXConfig extends TalonSRXConfiguration {
    public boolean invert;
    public boolean invertEncoder;
    public double ticksPerInch;
    public NeutralMode neutralMode;
    public Deadband deadband;
    public double maxTickVelocity;
    public Encoder encoder;
    public int lowTickMag;
    public int highTickMag;
    public Boolean crossZeroMag;
    public double zeroPosition;
    public boolean debug;

    public enum Encoder {
        USDigital, CTREMag;

        void applyConfig(BetterTalonSRXConfig config) {
            // Setting config.primaryPID.selectedFeedbackSensor(FeedbackDevice.QuadEncoder) is same
            // as TalonSRX.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, timeout);

            switch (this) {
                case USDigital:
                    config.primaryPID.selectedFeedbackSensor = FeedbackDevice.QuadEncoder;
                    config.auxiliaryPID.selectedFeedbackSensor = FeedbackDevice.QuadEncoder;
                    break;
                case CTREMag:
                    config.primaryPID.selectedFeedbackSensor =
                            FeedbackDevice.CTRE_MagEncoder_Relative;
                    config.auxiliaryPID.selectedFeedbackSensor =
                            FeedbackDevice.CTRE_MagEncoder_Absolute;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    public BetterTalonSRXConfig() {
        ticksPerInch = 0;
        invert = false;
        invertEncoder = false;
        neutralMode = NeutralMode.Brake;
        deadband = new Deadband(0.125, 0); // Warning: 0 deadband!;
        maxTickVelocity = 0;
        encoder = Encoder.USDigital;
        lowTickMag = 0;
        highTickMag = 0;
        crossZeroMag = null;
        zeroPosition = 0;
        debug = false;

        // Set defaults below for non-BetterTalonSRXConfig options

        openloopRamp = 0.1;
        closedloopRamp = 0.1;
        nominalOutputForward = 0;
        nominalOutputReverse = 0;
        clearPositionOnLimitR = true;
        // Probably only good if not using high current things. Drivetrain + Compressor > 12V
        voltageCompSaturation = 12;
        feedbackNotContinuous = true; // Do not wrap absolute 4095 -> 4096. Always 4095 -> 0
        peakCurrentLimit = 0;

        // Todo: current limit, voltage compensation
    }

    void prepare() {
        if (slot0.kF == 0 && maxTickVelocity != 0) {
            slot0.kF = 1023.0 / maxTickVelocity;
        }

        encoder.applyConfig(this);
    }
}
