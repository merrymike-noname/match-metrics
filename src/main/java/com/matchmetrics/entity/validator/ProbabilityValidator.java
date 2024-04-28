package com.matchmetrics.entity.validator;

import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.exception.InvalidProbabilityException;
import org.springframework.stereotype.Component;

@Component
public class ProbabilityValidator {
    public void validateProbability(ProbabilityGetDto dto) {
        float sum = dto.getHomeTeamWin() + dto.getDraw() + dto.getAwayTeamWin();
        if (sum != 1) {
            throw new InvalidProbabilityException(sum);
        }
    }
}
