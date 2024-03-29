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
                Arguments.of(new Task04(), "04", 5625994),
                Arguments.of(new Task05(), "05", 2254686),
                Arguments.of(new Task06(), "06", 27340847),
                Arguments.of(new Task07(), "07", 253473930),
                Arguments.of(new Task08(), "08", 9064949303801L),
                Arguments.of(new Task09(), "09", 1211),
                Arguments.of(new Task10(), "10", 355),
                Arguments.of(new Task11(), "11", 702152204842L),
                Arguments.of(new Task12(), "12", 6512849198636L),
                Arguments.of(new Task13(), "13", 41566)
        );
    }
}
