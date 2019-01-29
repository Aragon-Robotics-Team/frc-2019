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

    public static void byteArrayOutput(Grip pipeline) {

        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("datatable");
        xEntry = table.getEntry("X");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(pipeline.filterContoursOutput());
            out.flush();
            byte[] ByteOutput = bos.toByteArray();

            xEntry.setRaw(ByteOutput);

        } catch (IOException ex) {

        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }
    }
}
