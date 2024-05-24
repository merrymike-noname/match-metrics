package com.matchmetrics.service.implementation;

import com.matchmetrics.client.TeamCsvClient;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.entity.mapper.team.TeamGetMapper;
import com.matchmetrics.entity.mapper.team.TeamNestedMapper;
import com.matchmetrics.exception.InvalidDataException;
import com.matchmetrics.exception.TeamAlreadyExistsException;
import com.matchmetrics.exception.TeamDoesNotExistException;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.service.TeamService;
import com.matchmetrics.util.BindingResultInspector;
import com.matchmetrics.util.PageableCreator;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
    private final PageableCreator pageableCreator;
    private final BindingResultInspector bindingResultInspector;
    private final TeamCsvClient teamCsvClient;

    private final TeamRepository teamRepository;
    private final TeamGetMapper teamGetMapper;
    private final TeamNestedMapper teamNestedMapper;

    public TeamServiceImpl(TeamRepository teamRepository,
                           TeamGetMapper teamGetMapper,
                           TeamNestedMapper teamNestedMapper,
                           PageableCreator pageableCreator,
                           BindingResultInspector bindingResultInspector,
                           TeamCsvClient teamCsvClient) {
        this.teamRepository = teamRepository;
        this.teamGetMapper = teamGetMapper;
        this.teamNestedMapper = teamNestedMapper;
        this.pageableCreator = pageableCreator;
        this.bindingResultInspector = bindingResultInspector;
        this.teamCsvClient = teamCsvClient;
    }

    @Override
    public List<TeamGetDto> getAllTeams(Integer page, Integer perPage, String sortBy) {
        Pageable pageable = pageableCreator.createPageable(page, perPage, sortBy, Team.class);

        return teamRepository.findAll(pageable).getContent().stream()
                .map(teamGetMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TeamGetDto> getTeamsByCriteria(String name, String country,
                                               Float elo, Integer page,
                                               Integer perPage, String sortBy) {
        Pageable pageable = pageableCreator.createPageable(page, perPage, sortBy, Team.class);
        Specification<Team> spec = createSpecification(name, country, elo);
        Page<Team> teams = teamRepository.findAll(spec, pageable);

        if (teams.isEmpty()) {
            //logger.warn("No teams found with the given criteria. Name: {}, Country: {}, Elo: {}", name, country, elo);
            if (name != null && country == null && elo == null) {
                TeamNestedDto teamFromRemote = teamCsvClient.getTeamFromRemote(name);
                teams = new PageImpl<>(List.of(teamRepository.save(teamNestedMapper.toEntity(teamFromRemote))));
            }
        }

        return teams.getContent().stream()
                .map(teamGetMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TeamGetDto> getTeamsComparedByName(String homeTeamName, String awayTeamName) {
        if (homeTeamName.equals(awayTeamName)) {
            logger.error("Team names are the same: {}", homeTeamName);
            throw new InvalidDataException("Team names are the same");
        }

        Optional<Team> homeTeamEntity = teamRepository.findTeamByName(homeTeamName);
        Optional<Team> awayTeamEntity = teamRepository.findTeamByName(awayTeamName);

        if (homeTeamEntity.isEmpty()) {
            TeamNestedDto teamFromRemote = teamCsvClient.getTeamFromRemote(homeTeamName);
            Team homeTeamSaved = teamRepository.save(teamNestedMapper.toEntity(teamFromRemote));
            homeTeamSaved.setHomeMatches(new ArrayList<>());
            homeTeamSaved.setAwayMatches(new ArrayList<>());
            homeTeamEntity = Optional.of(homeTeamSaved);
        }

        if (awayTeamEntity.isEmpty()) {
            TeamNestedDto teamFromRemote = teamCsvClient.getTeamFromRemote(awayTeamName);
            Team awayTeamSaved = teamRepository.save(teamNestedMapper.toEntity(teamFromRemote));
            awayTeamSaved.setHomeMatches(new ArrayList<>());
            awayTeamSaved.setAwayMatches(new ArrayList<>());
            awayTeamEntity = Optional.of(awayTeamSaved);
        }

        if (homeTeamEntity.get().getId() == awayTeamEntity.get().getId()) {
            logger.error("Teams are the same: {}", homeTeamName);
            throw new InvalidDataException("Teams are the same");
        }

        TeamGetDto teamHomeDto = teamGetMapper.toDto(homeTeamEntity.get());
        TeamGetDto teamAwayDto = teamGetMapper.toDto(awayTeamEntity.get());

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

        bindingResultInspector.checkBindingResult(bindingResult);

        if (teamRepository.existsByName(team.getName())) {
            logger.error("Team with name {} already exists", team.getName());
            throw new TeamAlreadyExistsException(team.getName());
        }

        Team teamEntity = teamNestedMapper.toEntity(team);
        return teamGetMapper.toDto(teamRepository.save(teamEntity));
    }

    @Override
    public TeamGetDto updateTeam(int id, TeamNestedDto team, BindingResult bindingResult) {

        bindingResultInspector.checkBindingResult(bindingResult);

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