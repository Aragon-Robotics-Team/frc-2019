package frc.robot.vision;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTable;
import frc.robot.vision.Grip;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.io.IOException;

public class ByteArrayOutput {
    private static NetworkTableInstance inst;
    private static NetworkTable table;
    private static NetworkTableEntry xEntry;

    public static void setNetworkObject(Object obj, String tableName, String entryName) {

        inst = NetworkTableInstance.getDefault();
        table = inst.getTable(tableName);
        xEntry = table.getEntry(entryName);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            byte[] ByteOutput = bos.toByteArray();

            xEntry.setRaw(ByteOutput);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void setNetworkDoubleArr(double[] doubleArr, String tableName, String entryName) {
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable(tableName);
        xEntry = table.getEntry(entryName);


    }
}
