package com.matchmetrics.repository;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.searchCriteria.MatchSearchCriteria;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer>, JpaSpecificationExecutor<Match> {

    @EntityGraph(attributePaths = { "homeTeam", "awayTeam", "probability" })
    default List<Match> findMatches(MatchSearchCriteria searchCriteria) {
        return findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Fetch<Object, Object> homeTeamFetch = root.fetch("homeTeam", JoinType.LEFT);
            Fetch<Object, Object> awayTeamFetch = root.fetch("awayTeam", JoinType.LEFT);
            Join<Object, Object> homeTeam = (Join<Object, Object>) homeTeamFetch;
            Join<Object, Object> awayTeam = (Join<Object, Object>) awayTeamFetch;

            root.fetch("probability", JoinType.LEFT);
            if (searchCriteria.getTeam() != null) {
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
