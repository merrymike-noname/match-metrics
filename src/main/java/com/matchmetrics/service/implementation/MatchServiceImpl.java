package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.dto.main.MatchMainDto;
import com.matchmetrics.entity.mapper.main.MatchMainMapper;
import com.matchmetrics.entity.searchCriteria.MatchSearchCriteria;
import com.matchmetrics.exception.DateConversionException;
import com.matchmetrics.exception.MatchDoesNotExistException;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

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
        if (match.isEmpty()) throw new MatchDoesNotExistException(id);
        return matchMainMapper.toDto(match.get());
    }

    @Override
    public Match addMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match updateMatch(int id, Match match) {
        if (matchRepository.existsById(id)) {
            match.setId(id);
            return matchRepository.save(match);
        } else {
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
        if(strDate == null) {
            return null;
        }
        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            throw new DateConversionException(e);
        }
    }
}
