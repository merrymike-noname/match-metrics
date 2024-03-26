package com.matchmetrics;

import com.matchmetrics.entity.dto.match.MatchMainDto;
import com.matchmetrics.exception.NotEnoughDataException;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.service.implementation.MatchServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class MatchServiceImplTest {

    private final MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
    private final MatchServiceImpl matchService = new MatchServiceImpl(matchRepository, null);

    @Test
    public void testUpdateMatchError() {
        int id = 1;
        MatchMainDto match = new MatchMainDto();
        BindingResult bindingResult = org.mockito.Mockito.mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("match", "error message")));
        when(matchRepository.existsById(id)).thenReturn(true);

        assertThrows(NotEnoughDataException.class, () -> matchService.updateMatch(id, match, bindingResult));
    }
}