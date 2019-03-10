package frc.robot.util;

import java.lang.reflect.Field;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.map.RobotMap;

public abstract class BetterRobot extends TimedRobot {

    // Init SendableMaster before calling robotInit
    // Will only work if all sendables created before robotInit
    public void startCompetition() {
        // Get actual class
        final Class<? extends BetterRobot> clazz = getClass();

        // For each field in class
        for (final Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                // If the field is a BetterSendable
                if (BetterSendable.class.isAssignableFrom(field.getType())) {
                    // Add to SendableMaster
                    SendableMaster.getInstance().add((BetterSendable) field.get(this));
                }

                if (RobotMap.class.isAssignableFrom(field.getType())) {
                    ((RobotMap) field.get(this)).init();
                }
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

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
