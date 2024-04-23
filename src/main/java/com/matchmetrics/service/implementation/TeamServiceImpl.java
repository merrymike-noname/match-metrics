package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.entity.mapper.team.TeamGetMapper;
import com.matchmetrics.entity.mapper.team.TeamNestedMapper;
import com.matchmetrics.exception.FieldDoesNotExistException;
import com.matchmetrics.exception.InvalidDataException;
import com.matchmetrics.exception.TeamAlreadyExistsException;
import com.matchmetrics.exception.TeamDoesNotExistException;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.service.TeamService;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;
    private final TeamGetMapper teamGetMapper;
    private final TeamNestedMapper teamNestedMapper;

    public TeamServiceImpl(TeamRepository teamRepository,
                           TeamGetMapper teamGetMapper,
                           TeamNestedMapper teamNestedMapper) {
        this.teamRepository = teamRepository;
        this.teamGetMapper = teamGetMapper;
        this.teamNestedMapper = teamNestedMapper;
    }

    @Override
    public List<TeamGetDto> getAllTeams(Integer page, Integer perPage, String sortBy) {
        Pageable pageable = createPageable(page, perPage, sortBy);

        return teamRepository.findAll(pageable).getContent().stream()
                .map(teamGetMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TeamGetDto> getTeamsByCriteria(String name, String country,
                                               Float elo, Integer page,
                                               Integer perPage, String sortBy) {
        Pageable pageable = createPageable(page, perPage, sortBy);
        Specification<Team> spec = createSpecification(name, country, elo);
        Page<Team> teams = teamRepository.findAll(spec, pageable);

        if (teams.isEmpty()) {
            logger.warn("No teams found with the given criteria. Name: {}, Country: {}, Elo: {}", name, country, elo);
        }

        return teams.getContent().stream()
                .map(teamGetMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TeamNestedDto> getTeamsComparedByName(String teamHome, String teamAway) {
        Optional<Team> teamHomeEntity = teamRepository.findTeamByName(teamHome);
        Optional<Team> teamAwayEntity = teamRepository.findTeamByName(teamAway);

        if (teamHomeEntity.isEmpty() ) {
            logger.error("First team does not exist: {}", teamHome);
            throw new TeamDoesNotExistException("First team does not exist");
        }

        if (teamAwayEntity.isEmpty()) {
            logger.error("Second team does not exist: {}", teamAway);
            throw new TeamDoesNotExistException("Second team does not exist");
        }

        if (teamHomeEntity.get().getId() == teamAwayEntity.get().getId()) {
            logger.error("Teams are the same: {}", teamHome);
            throw new InvalidDataException("Teams are the same");
        }

        TeamNestedDto teamHomeDto = teamNestedMapper.toDto(teamHomeEntity.get());
        TeamNestedDto teamAwayDto = teamNestedMapper.toDto(teamAwayEntity.get());

        return List.of(teamHomeDto, teamAwayDto);
    }

    @Override
    public TeamGetDto getTeamById(int id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) {
            logger.error("Team with ID {} not found", id);
            throw new TeamDoesNotExistException(id);
        }
        return teamGetMapper.toDto(team.get());
    }

    @Override
    public TeamGetDto createTeam(TeamNestedDto team, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            String errorMessage = String.join(", ", errorMessages);
            logger.error("Error occurred while creating team: {}", errorMessage);
            throw new InvalidDataException(errorMessage);
        }
        if (teamRepository.existsByName(team.getName())) {
            logger.error("Team with name {} already exists", team.getName());
            throw new TeamAlreadyExistsException(team.getName());
        }

        Team teamEntity = teamNestedMapper.toEntity(team);
        return teamGetMapper.toDto(teamRepository.save(teamEntity));
    }

    @Override
    public TeamGetDto updateTeam(int id, TeamNestedDto team, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            String errorMessage = String.join(", ", errorMessages);
            logger.error("Error occurred while updating team: {}", errorMessage);
            throw new InvalidDataException(errorMessage);
        }
        Optional<Team> existingTeam = teamRepository.findById(id);
        if (existingTeam.isEmpty()) {
            logger.error("Team with ID {} not found", id);
            throw new TeamDoesNotExistException(id);
        }
        if (!existingTeam.get().getName().equals(team.getName()) &&
                teamRepository.existsByName(team.getName())) {
            logger.error("Team with name {} already exists, you can't update with it.", team.getName());
            throw new TeamAlreadyExistsException(team.getName());
        }

        Team teamEntity = existingTeam.get();
        teamEntity.setName(team.getName());
        teamEntity.setCountry(team.getCountry());
        teamEntity.setElo(team.getElo());

        return teamGetMapper.toDto(teamRepository.save(teamEntity));
    }

    @Override
    public void deleteTeam(int id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
        } else {
            logger.error("Team with ID {} not found", id);
            throw new TeamDoesNotExistException(id);
        }
    }

    private Pageable createPageable(int page, int perPage, String sortBy) {
        if (!sortBy.equals("default") && !checkIfFieldExists(sortBy)) {
            logger.error("Error occurred while trying to find a '{}' field in {}", sortBy, Team.class.getName());
            throw new FieldDoesNotExistException(sortBy, Match.class);
        }
        Sort sort = sortBy.equals("default") ? Sort.unsorted() : Sort.by(sortBy);
        return PageRequest.of(page, perPage, sort);
    }

    private boolean checkIfFieldExists(String fieldName) {
        return Arrays.stream(Team.class.getDeclaredFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }

    private Specification<Team> createSpecification(String name, String country, Float elo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("name"), name));
            }
            if (country != null && !country.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("country"), country));
            }
            if (elo != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("elo"), elo));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}