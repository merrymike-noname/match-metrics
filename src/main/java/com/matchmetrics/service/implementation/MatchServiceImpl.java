package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.dto.match.MatchMainDto;
import com.matchmetrics.entity.mapper.match.MatchMainMapper;
import com.matchmetrics.entity.searchCriteria.MatchSearchCriteria;
import com.matchmetrics.exception.DateConversionException;
import com.matchmetrics.exception.MatchDoesNotExistException;
import com.matchmetrics.exception.InvalidDataException;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.service.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final Logger logger = LoggerFactory.getLogger(MatchServiceImpl.class);

    private final MatchRepository matchRepository;
    private final MatchMainMapper matchMainMapper;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, MatchMainMapper matchMainMapper) {
        this.matchRepository = matchRepository;
        this.matchMainMapper = matchMainMapper;
    }

    @Override
    public List<MatchMainDto> getAllMatches() {
        return matchRepository.findAll().stream().map(matchMainMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MatchMainDto getMatchById(int id) {
        Optional<Match> match = matchRepository.findById(id);
        if (match.isEmpty()) {
            logger.error("Match with ID {} not found", id);
            throw new MatchDoesNotExistException(id);
        }
        return matchMainMapper.toDto(match.get());
    }

    @Override
    public MatchMainDto addMatch(MatchMainDto match, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            logger.error("Error occurred while adding match: {}", errorMessage);
            throw new InvalidDataException(errorMessage.toString());
        }
        return matchMainMapper.toDto(matchRepository.save(matchMainMapper.toEntity(match)));
    }

    @Override
    public MatchMainDto updateMatch(int id, MatchMainDto match, BindingResult bindingResult) {
        if (matchRepository.existsById(id)) {
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder();
                bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
                logger.error("Error occurred while updating match: {}", errorMessage);
                throw new InvalidDataException(errorMessage.toString());
            }
            Match matchEntity = matchMainMapper.toEntity(match);
            matchEntity.setId(id);
            return matchMainMapper.toDto(matchRepository.save(matchEntity));
        } else {
            logger.error("Match with ID {} not found", id);
            throw new MatchDoesNotExistException(id);
        }
    }

    @Override
    public void deleteMatch(int id) {
        matchRepository.deleteById(id);
    }

    @Override
    public List<MatchMainDto> getMatchesByCriteria(String team, Boolean isHome, String date, String league) {
        return matchRepository.findMatches(new MatchSearchCriteria(team, isHome, convertStringToDate(date), league))
                .stream().map(matchMainMapper::toDto).collect(Collectors.toList());
    }

    public Date convertStringToDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            logger.error("Error occurred while converting string to date: {}", e.getMessage());
            throw new DateConversionException(e);
        }
    }
}
