package com.matchmetrics.util.validator;

import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.exception.InvalidProbabilityException;
import org.springframework.stereotype.Component;

@Component
public class ProbabilityValidator {
    public void validateProbability(ProbabilityGetDto dto) {
        float sum = dto.getHomeTeamWin() + dto.getDraw() + dto.getAwayTeamWin();
        if (sum < 0.9999991 || sum > 1.0000001) {
            throw new InvalidProbabilityException(sum);
        }
    }
}
