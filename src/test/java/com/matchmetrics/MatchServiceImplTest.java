package com.matchmetrics;

import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.exception.InvalidDataException;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.service.implementation.MatchServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    public void testUpdateMatchError() {
        int id = 1;
        MatchAddUpdateDto match = new MatchAddUpdateDto();
        BindingResult bindingResult = org.mockito.Mockito.mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("match", "error message")));
        when(matchRepository.existsById(id)).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> matchService.updateMatch(id, match, bindingResult));
    }
}