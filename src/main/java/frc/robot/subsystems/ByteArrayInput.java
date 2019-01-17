package frc.robot.subsystems;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.IOException;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class ByteArrayInput {
    public static void main(String[] args) {

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("datatable");
        NetworkTableEntry xEntry = table.getEntry("X");

        byte[] MyBytes = xEntry;

        ByteArrayInputStream bis = new ByteArrayInputStream(MyBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object GripImage = in.readObject();

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }

    }
}

