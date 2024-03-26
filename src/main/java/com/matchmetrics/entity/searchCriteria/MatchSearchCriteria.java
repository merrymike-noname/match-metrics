package com.matchmetrics.entity.searchCriteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchSearchCriteria {
    private String team;
    private Boolean isHome;
    private Date date;
    private String league;
}
