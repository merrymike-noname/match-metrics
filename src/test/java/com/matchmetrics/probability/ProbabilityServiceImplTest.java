package com.matchmetrics.probability;

import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapperImpl;
import com.matchmetrics.exception.ProbabilityDoesNotExistException;
import com.matchmetrics.repository.ProbabilityRepository;
import com.matchmetrics.service.implementation.ProbabilityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProbabilityServiceImplTest {
    @Mock
    private ProbabilityRepository probabilityRepository;

    @Mock
    private BindingResult bindingResult;

    @Spy
    private ProbabilityGetMapper probabilityGetMapper = new ProbabilityGetMapperImpl();

    @InjectMocks
    private ProbabilityServiceImpl probabilityService;

    @Test
    void testGetAllProbabilities() {
        List<Probability> probabilities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            probabilities.add(new Probability());
        }
        List<ProbabilityGetDto> expectedProbabilities = probabilities.stream().map(probabilityGetMapper::toDto).toList();

        when(probabilityRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(probabilities));

        List<ProbabilityGetDto> actualProbabilities = probabilityService.getAllProbabilities(1, 5, "default");
        assertThat(actualProbabilities).isEqualTo(expectedProbabilities);
    }

    @Test
    void testGetProbabilityById() {
        int id = 1;
        Probability probability = new Probability();
        ProbabilityGetDto expectedProbability = probabilityGetMapper.toDto(probability);

        when(probabilityRepository.findById(id)).thenReturn(Optional.of(probability));

        ProbabilityGetDto actualProbability = probabilityService.getProbabilityById(id);
        assertThat(actualProbability).isEqualTo(expectedProbability);
    }

    @Test
    void testGetProbabilityById_ThrowsException() {
        int id = 1;

        when(probabilityRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProbabilityDoesNotExistException.class, () -> probabilityService.getProbabilityById(id));
    }

    @Test
    void testCreateProbability() {
        Probability probabilityEntity = new Probability();
        ProbabilityGetDto dto = new ProbabilityGetDto();
        ProbabilityGetDto expected = probabilityGetMapper.toDto(probabilityEntity);

       // when(bindingResult.hasErrors()).thenReturn(false);
        when(probabilityGetMapper.toEntity(dto)).thenReturn(probabilityEntity);
        when(probabilityRepository.save(probabilityEntity)).thenReturn(probabilityEntity);

        ProbabilityGetDto createdProbability = probabilityService.createProbability(dto, bindingResult);
        assertThat(createdProbability).isEqualTo(expected);
    }

    @Test
    void testUpdateProbability() {
        int id = 1;
        ProbabilityGetDto dto = new ProbabilityGetDto();
        Probability existingProbability = new Probability();
        Probability updatedProbability = probabilityGetMapper.toEntity(dto);
        ProbabilityGetDto expected = probabilityGetMapper.toDto(updatedProbability);

        //   when(bindingResult.hasErrors()).thenReturn(false);
        when(probabilityRepository.findById(id)).thenReturn(Optional.of(existingProbability));
        when(probabilityRepository.save(existingProbability)).thenReturn(updatedProbability);

        ProbabilityGetDto actualProbability = probabilityService.updateProbability(id, dto, bindingResult);
        assertThat(actualProbability).isEqualTo(expected);
    }

    @Test
    void testDeleteProbability() {
        int id = 1;
        when(probabilityRepository.existsById(id)).thenReturn(true);
        doNothing().when(probabilityRepository).deleteById(id);
        probabilityService.deleteProbability(id);
        verify(probabilityRepository, times(1)).deleteById(id);

        when(probabilityRepository.existsById(id)).thenReturn(false);
        assertThrows(ProbabilityDoesNotExistException.class, () -> probabilityService.deleteProbability(id));
    }
}