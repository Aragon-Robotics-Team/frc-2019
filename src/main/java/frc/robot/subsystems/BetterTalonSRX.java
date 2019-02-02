package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.SpeedController;
import frc.robot.util.Deadband;

public class BetterTalonSRX extends TalonSRX implements SpeedController {
    double speed;
    static Deadband deadband = new Deadband(0.125, 0); // Warning: 0 deadband!;
    public static final int timeout = 30; // milliseconds

    public BetterTalonSRX(int deviceNumber, boolean invert) {
        super(deviceNumber);
        configFactoryDefault(timeout);
        setInverted(invert);
        configOpenloopRamp(2);
        this.setNeutralMode(NeutralMode.Brake);
    }

    public BetterTalonSRX(int deviceNumber) {
        this(deviceNumber, false);
    }

    public void set(double output) {
        speed = output;

        output = deadband.calc(output);

        set(ControlMode.PercentOutput, output);
    }

    public double get() {
        return speed;
    }

    public void disable() {
        set(0);
    }

    public void stopMotor() {
        set(0);
    }

    public void pidWrite(double output) {
        set(output);
    }
}
