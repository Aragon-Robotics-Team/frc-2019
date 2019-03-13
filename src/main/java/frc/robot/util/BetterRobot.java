package frc.robot.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.map.RobotMap;

public abstract class BetterRobot extends TimedRobot {
    private List<Disableable> disableables;
    private List<Command> commands;

    public static BetterRobot instance;

    public BetterRobot() {
        super();
        instance = this;
    }

    // Init SendableMaster before calling robotInit
    // Will only work if all sendables created before robotInit
    public void startCompetition() {
        System.out.println("Preparing to start robot...");
        disableables = new ArrayList<Disableable>(10); // Expected max, but can be more
        commands = new ArrayList<Command>(6); // Exepcted max: 1 / overridable periodic method

        // Get actual class
        final Class<? extends BetterRobot> clazz = getClass();

        // For each field in class
        for (final Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            // Add w/ SendableMaster if is BetterSendable
            castField(field, BetterSendable.class, obj -> SendableMaster.getInstance().add(obj));
            // Init OI if is RobotMap
            castField(field, RobotMap.class, map -> map.init());
            // Add to list if is Disableable
            castField(field, Disableable.class, disableable -> disableables.add(disableable));
            // Add to list if is Command
            castField(field, Command.class, command -> commands.add(command));
        }

        if (disableables.size() > 0) {
            System.out.println("Found " + disableables.size() + " disableables:");
            for (int index = 0; index < disableables.size(); index++) {
                System.out.println("    " + index + ". " + disableables.get(index));
            }
            System.out.println();
        }

        if (commands.size() > 0) {
            System.out.println("Found " + commands.size() + " commands:");
            for (int index = 0; index < commands.size(); index++) {
                System.out.println("    " + index + ". " + commands.get(index));
            }
            System.out.println();
        }

        System.out.println("Start robot");
        super.startCompetition();
    }

    private <T> void castField(Field field, Class<T> clazz, Consumer<T> run) {
        try {
            if (clazz.isAssignableFrom(field.getType())) {
                // Even though it says the cast is unchecked, the line above does check it
                run.accept((T) field.get(this));
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void addDisableable(Disableable disableable) {
        disableables.add(disableable);
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void addCommand(Command command, boolean enable) {
        if (enable) {
            addDisableable(EnableableCommand.of(command));
        } else {
            addCommand(command);
        }
    }

    /**
     * Override _disabledInit instead
     */
    public final void disabledInit() {
        System.out.println("Disable robot");
        for (Disableable disableable : disableables) {
            disableable.disable();
        }
        for (Command command : commands) {
            command.cancel();
        }
        _disabledInit();
    }

    /**
     * Override _enabledInit instead
     */
    private final void enabledInit() {
        for (Disableable disableable : disableables) {
            disableable.enable();
        }
        _enabledInit();
    }

    /**
     * Override _robotPeriodic instead
     */
    public final void robotPeriodic() {
        Scheduler.getInstance().run();
        _robotPeriodic();
    }

    /**
     * Override _autonomousInit instead
     */
    public final void autonomousInit() {
        System.out.println("Starting auto");
        enabledInit();
        _autonomousInit();
    }

    /**
     * Override _teleopInit instead
     */
    public final void teleopInit() {
        System.out.println("Starting teleop");
        enabledInit();
        _teleopInit();
    }

    /**
     * Override _testInit instead
     */
    public final void testInit() {
        System.out.println("Starting test");
        enabledInit();
        _testInit();
    }

    // Override all other methods to disable annoying prints

    public void robotInit() {
    }

    public void disablePeriodic() {
    }

    public void autonomousPeriodic() {
    }

    public void teleopPeriodic() {
    }

    public void testPeriodic() {
    }

    // Provide hooks to override

    public void _disabledInit() {
    }

    public void _enabledInit() {
    }

    public void _robotPeriodic() {
    }

    public void _autonomousInit() {
    }

    public void _teleopInit() {
    }

    public void _testInit() {
    }
}
