package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.searchCriteria.MatchSearchCriteria;
import com.matchmetrics.exceptions.MatchUpdateException;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match getMatchById(int id) {
        Optional<Match> match = matchRepository.findById(id);
        return match.orElse(null);
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
            throw new MatchUpdateException(id);
        }
    }

    @Override
    public void deleteMatch(int id) {
        matchRepository.deleteById(id);
    }

    // TODO: Methods below


    @Override
    public List<Match> getMatchesByCriteria(String team, Boolean isHome, String date, String league) {
        return matchRepository.findMatches(new MatchSearchCriteria(team, isHome, convertStringToDate(date), league));
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
            // todo custom exception
            e.printStackTrace();
            return null;
        }
    }
}
