package frc.robot.util;

public interface BetterSendable {
    // Call master.add(sendable) master.add(name, sendable)
    public void createSendable(SendableMaster master);

    public default String getTabName() {
        return "";
    }
}
