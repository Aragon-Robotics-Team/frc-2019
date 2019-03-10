package frc.robot.vision;

import java.util.function.Consumer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;

public class NetworkListener implements TableEntryListener {
    public Consumer<NetworkTableEntry> func;

    public NetworkListener(Consumer<NetworkTableEntry> func) {
        this.func = func;
    }

    @Override
    public void valueChanged(NetworkTable table, String key, NetworkTableEntry entry,
            NetworkTableValue val, int flags) {
        func.accept(entry);
    }
}
