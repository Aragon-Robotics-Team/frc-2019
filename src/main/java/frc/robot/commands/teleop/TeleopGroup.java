package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.commands.SetCompressorEnabled;
import frc.robot.commands.intake.CalibrateIntakeEncoder;
import frc.robot.commands.lift.ResetLiftEncoder;
import frc.robot.commands.rgb.RGBAutonomous;

public class TeleopGroup extends CommandGroup {

    public TeleopGroup() {
        addParallel(new RGBAutonomous());
        addParallel(new SetCompressorEnabled(() -> !Robot.m_oi.getSlowMode()));
        addParallel(new CalibrateIntakeEncoder());
        addSequential(new ResetLiftEncoder());
        addSequential(new ControlArcadeDrivetrain());
    }
}
