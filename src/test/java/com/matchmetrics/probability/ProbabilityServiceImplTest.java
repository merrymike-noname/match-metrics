package com.matchmetrics.probability;

import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapperImpl;
import com.matchmetrics.entity.validator.ProbabilityValidator;
import com.matchmetrics.exception.AssociatedProbabilityException;
import com.matchmetrics.exception.InvalidDataException;
import com.matchmetrics.exception.ProbabilityDoesNotExistException;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.repository.ProbabilityRepository;
import com.matchmetrics.service.implementation.ProbabilityServiceImpl;
import com.matchmetrics.util.BindingResultInspector;
import com.matchmetrics.util.PageableCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Arrays;
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
    private MatchRepository matchRepository;

    @Mock
    private BindingResult bindingResult;

    @Spy
    private ProbabilityGetMapper probabilityGetMapper = new ProbabilityGetMapperImpl();

    @Spy
    private final PageableCreator pageableCreator = new PageableCreator();

    @Spy
    private final ProbabilityValidator probabilityValidator = new ProbabilityValidator();

    @Spy
    private final BindingResultInspector bindingResultInspector = new BindingResultInspector();

    @InjectMocks
    private ProbabilityServiceImpl probabilityService;

    @Test
    void testGetAllProbabilities() {
        List<Probability> probabilities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            probabilities.add(new Probability(0.1f, 0.3f, 0.6f));
        }
        List<ProbabilityGetDto> expectedProbabilities = probabilities.stream().map(probabilityGetMapper::toDto).toList();

        when(probabilityRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(probabilities));

        List<ProbabilityGetDto> actualProbabilities = probabilityService.getAllProbabilities(1, 5, "default");
        assertThat(actualProbabilities).isEqualTo(expectedProbabilities);
    }

    @Test
    void testGetProbabilityById() {
        int id = 1;
        Probability probability = new Probability(0.1f, 0.3f, 0.6f);
        ProbabilityGetDto expectedProbability = probabilityGetMapper.toDto(probability);

        when(probabilityRepository.findById(id)).thenReturn(Optional.of(probability));

        ProbabilityGetDto actualProbability = probabilityService.getProbabilityById(id);
        assertThat(actualProbability).isEqualTo(expectedProbability);
    }

    @Test
    void testGetProbabilityById_ThrowsExceptions() {
        int id = 1;

        when(probabilityRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProbabilityDoesNotExistException.class, () -> probabilityService.getProbabilityById(id));
    }

    @Test
    void testGetProbabilityByCriteria() {
        List<Probability> probabilities = Arrays.asList(
                new Probability(0.3f, 0.2f, 0.5f),
                new Probability(0.4f, 0.2f, 0.4f),
                new Probability(0.5f, 0.2f, 0.3f)
        );
        List<ProbabilityGetDto> expectedProbabilities =
                probabilities.stream().map(probabilityGetMapper::toDto).toList();

        when(probabilityRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(probabilities));

        List<ProbabilityGetDto> actualProbabilities =
                probabilityService.getProbabilitiesByCriteria(0.2f, true, 1, 5, "default");
        assertThat(actualProbabilities).isEqualTo(expectedProbabilities);
        verify(probabilityRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testCreateProbability() {
        Probability probabilityEntity = new Probability(0.3f, 0.3f, 0.4f);
        ProbabilityGetDto dto = new ProbabilityGetDto(0.3f, 0.3f, 0.4f);
        ProbabilityGetDto expected = probabilityGetMapper.toDto(probabilityEntity);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(probabilityGetMapper.toEntity(dto)).thenReturn(probabilityEntity);
        when(probabilityRepository.save(probabilityEntity)).thenReturn(probabilityEntity);

        ProbabilityGetDto createdProbability = probabilityService.createProbability(dto, bindingResult);
        assertThat(createdProbability).isEqualTo(expected);
    }

    @Test
    void testCreateProbability_ThrowsExceptions() {
        ProbabilityGetDto dto = new ProbabilityGetDto(0.3f, 0.3f, 0.4f);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors())
                .thenReturn(List.of(new ObjectError("team", "error message")));
        assertThrows(InvalidDataException.class, () -> probabilityService.createProbability(dto, bindingResult));
    }

    @Test
    void testUpdateProbability() {
        int id = 1;
        ProbabilityGetDto dto = new ProbabilityGetDto(0.3f, 0.3f, 0.4f);
        Probability existingProbability = new Probability();
        Probability updatedProbability = probabilityGetMapper.toEntity(dto);
        ProbabilityGetDto expected = probabilityGetMapper.toDto(updatedProbability);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(probabilityRepository.findById(id)).thenReturn(Optional.of(existingProbability));
        when(probabilityRepository.save(existingProbability)).thenReturn(updatedProbability);

        ProbabilityGetDto actualProbability = probabilityService.updateProbability(id, dto, bindingResult);
        assertThat(actualProbability).isEqualTo(expected);
    }

    @Test
    void testUpdateProbability_ThrowsExceptions() {
        int id = 1;
        ProbabilityGetDto dto = new ProbabilityGetDto(0.3f, 0.3f, 0.4f);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors())
                .thenReturn(List.of(new ObjectError("team", "error message")));
        assertThrows(InvalidDataException.class, () -> probabilityService.updateProbability(id, dto, bindingResult));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(probabilityRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ProbabilityDoesNotExistException.class, () -> probabilityService.updateProbability(id, dto, bindingResult));
    }

    @Test
    void testDeleteProbability() {
        int id = 1;
        when(probabilityRepository.existsById(id)).thenReturn(true);
        when(matchRepository.existsByProbabilityId(id)).thenReturn(false);
        doNothing().when(probabilityRepository).deleteById(id);
        probabilityService.deleteProbability(id);
        verify(probabilityRepository, times(1)).deleteById(id);

        when(probabilityRepository.existsById(id)).thenReturn(false);
        assertThrows(ProbabilityDoesNotExistException.class,
                () -> probabilityService.deleteProbability(id));

        when(probabilityRepository.existsById(id)).thenReturn(true);
        when(matchRepository.existsByProbabilityId(id)).thenReturn(true);
        assertThrows(AssociatedProbabilityException.class,
                () -> probabilityService.deleteProbability(id));
    }
}