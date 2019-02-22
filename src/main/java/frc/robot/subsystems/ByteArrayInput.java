package frc.robot.subsystems;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class ByteArrayInput {
    public Object GripImage;

    public static Object deserialize(Object placeholder, byte[] b) {
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        try {
            ObjectInputStream in = new ObjectInputStream(bis);
            placeholder = in.readObject();
            return placeholder;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Object getNetworkObject(Object placeholder, String tableName, String entryName) {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable(tableName);
        NetworkTableEntry xEntry = table.getEntry(entryName);

        byte[] b = new byte[0];
        b = xEntry.getRaw(b);

        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        try {
            ObjectInputStream in = new ObjectInputStream(bis);
            placeholder = in.readObject();
            return placeholder;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }

        // finally {
        // try {
        // if (in != null) {
        // in.close();
        // }
        // } catch (IOException ex) {
        // // ignore close exception
        // }
        // }

    }
}
