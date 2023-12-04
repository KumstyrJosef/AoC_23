import org.example.Task01;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitTest {

    @Test
    void test_01() throws IOException {
        var result = new Task01().countCalibrationValueForFile("target/test-classes/input01");
        assertEquals(54249, result);
    }
}
