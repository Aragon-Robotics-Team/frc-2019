package frc.robot.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;

public class NetworkListener implements TableEntryListener {
    public Runnable func;

    public NetworkListener(Runnable func) {
        this.func = func;
    }

    @Override
    public void valueChanged(NetworkTable table, String key, NetworkTableEntry entry, NetworkTableValue val,
            int flags) {

    }
}