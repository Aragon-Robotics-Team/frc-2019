package frc.robot.util;

import org.mockito.Mockito;

public class Mock {
    public static <T> T mock(Class<T> classToMock) {
        return Mockito.mock(classToMock, Mockito.withSettings().stubOnly());
    }
}
