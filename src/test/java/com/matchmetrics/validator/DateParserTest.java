package com.matchmetrics.validator;

import com.matchmetrics.util.DateParser;
import com.matchmetrics.exception.DateConversionException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DateParserTest {

    private final DateParser dateParser = new DateParser();

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void validate(String date, boolean valid, String exceptionMessage) {
        if (valid) {
            assertDoesNotThrow(() -> dateParser.validate(date));
        } else {
            Exception exception = assertThrows(DateConversionException.class, () -> dateParser.validate(date));
            assertTrue(exception.getMessage().contains(exceptionMessage));
        }
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("2024-08-24", true, ""),
                Arguments.of("2024-12-30", true, ""),
                Arguments.of("2024-30-30", false, ""),
                Arguments.of("2024-10-40", false, ""),
                Arguments.of(null, false, "Date cannot be null or empty"),
                Arguments.of("", false, "Date cannot be null or empty")
        );
    }

    // todo add test for successful parsing
}
