package com.matchmetrics.repository;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.searchCriteria.MatchSearchCriteria;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer>, JpaSpecificationExecutor<Match> {

    default List<Match> findMatches(MatchSearchCriteria searchCriteria) {
        return findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchCriteria.getTeam() != null) {
                Join<Object, Object> homeTeam = root.join("homeTeam");
                Join<Object, Object> awayTeam = root.join("awayTeam");

                if (searchCriteria.getIsHome() != null) {
                    predicates.add(
                            searchCriteria.getIsHome() ?
                                    criteriaBuilder.equal(homeTeam.get("name"), searchCriteria.getTeam()) :
                                    criteriaBuilder.equal(awayTeam.get("name"), searchCriteria.getTeam())
                    );
                } else {
                    predicates.add(
                            criteriaBuilder.or(
                                    criteriaBuilder.equal(homeTeam.get("name"), searchCriteria.getTeam()),
                                    criteriaBuilder.equal(awayTeam.get("name"), searchCriteria.getTeam())
                            )
                    );
                }
            }

            if (searchCriteria.getDate() != null)
                predicates.add(criteriaBuilder.equal(root.get("date"), searchCriteria.getDate()));

            if (searchCriteria.getLeague() != null)
                predicates.add(criteriaBuilder.equal(root.get("league"), searchCriteria.getLeague()));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
