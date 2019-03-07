package frc.robot.util;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

public class BetterRobot extends TimedRobot {
    public void startCompetition() {
        // init SendableMaster
        super.startCompetition();
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    public void testPeriodic() {
        Scheduler.getInstance().run();
    }
}
