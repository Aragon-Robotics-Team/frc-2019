package frc.robot.subsystems;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class ByteArrayInput {
    public static <T> T deserialize(T placeholder, byte[] bytes) {
        ByteArrayInputStream byteStream = null;
        ObjectInputStream objectStream = null;

        T result = placeholder;

        try {
            byteStream = new ByteArrayInputStream(bytes);
            objectStream = new ObjectInputStream(byteStream);

            result = (T) objectStream.readObject();

        } catch (IOException | ClassNotFoundException | ClassCastException ex) {
            ex.printStackTrace();
        } finally {
            try {
                byteStream.close();
            } catch (IOException | NullPointerException ex) {
            }
            try {
                objectStream.close();
            } catch (IOException | NullPointerException ex) {
            }
        }

        return result;
    }

    public static <T> T getNetworkObject(T placeholder, String tableName, String entryName) {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable(tableName);
        NetworkTableEntry xEntry = table.getEntry(entryName);

        byte[] bytes = xEntry.getRaw(new byte[0]);

        return deserialize(placeholder, bytes);
    }
}
