package com.matchmetrics.validator;

import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.entity.validator.ProbabilityValidator;
import com.matchmetrics.exception.InvalidProbabilityException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProbabilityValidatorTest {

    private final ProbabilityValidator probabilityValidator = new ProbabilityValidator();

    @ParameterizedTest
    @MethodSource("provideTestCases")
    public void validateProbability(ProbabilityGetDto dto, boolean valid) {
        if (valid) {
            assertDoesNotThrow(() -> probabilityValidator.validateProbability(dto));
        } else {
            assertThrows(InvalidProbabilityException.class, () -> probabilityValidator.validateProbability(dto));
        }
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(new ProbabilityGetDto(0.3F, 0.3F, 0.4F), true),
                Arguments.of(new ProbabilityGetDto(0.3F, 0.3F, 0.5F), false),
                Arguments.of(new ProbabilityGetDto(0.1F, 0.5F, 0.4F), true),
                Arguments.of(new ProbabilityGetDto(0.1F, 0.5F, 0.5F), false),
                Arguments.of(new ProbabilityGetDto(0.33F, 0.33F, 0.33F), false)
        );
    }
}
