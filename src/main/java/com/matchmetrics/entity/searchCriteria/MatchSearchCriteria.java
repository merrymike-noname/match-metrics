package com.matchmetrics.entity.searchCriteria;

import java.util.Date;

public class MatchSearchCriteria {
    private String team;
    private Boolean isHome;
    private Date date;
    private String league;

    public MatchSearchCriteria(String team, Boolean isHome, Date date, String league) {
        this.team = team;
        this.isHome = isHome;
        this.date = date;
        this.league = league;
    }

    public MatchSearchCriteria() {
    }

    public String getTeam() {
        return this.team;
    }

    public Boolean getIsHome() {
        return this.isHome;
    }

    public Date getDate() {
        return this.date;
    }

    public String getLeague() {
        return this.league;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setIsHome(Boolean isHome) {
        this.isHome = isHome;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLeague(String league) {
        this.league = league;
    }
}
