package frc.robot.util;

import java.util.function.DoubleConsumer;

public class Parameterize {
    public DoubleConsumer floatArraySetterWithDouble(float[] array, int index, Runnable runAfter) {
        return new DoubleConsumer() {
            public void accept(double val) {
                array[index] = (float) val;
                runAfter.run();
            }
        };
    }
}
