package frc.robot.commands.test;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestGroup extends CommandGroup {
    public TestGroup() {
        addParallel(new TestNavX());
    }
}
