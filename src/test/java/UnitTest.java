import org.example.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitTest {

    @ParameterizedTest
    @MethodSource("getParameters")
    void test(Task task, String input, long expected) throws IOException {
        var result = task.solve("target/test-classes/input" + input);
        assertEquals(expected, result);
    }

    static Stream<Arguments> getParameters() {
        return Stream.of(
                Arguments.of(new Task01(), "01", 54249),
                Arguments.of(new Task02(), "02", 68638),
                Arguments.of(new Task03(), "03", 87263515),
                Arguments.of(new Task04(), "04", 5625994)
        );
    }
}
