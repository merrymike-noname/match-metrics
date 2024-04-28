alter table Probability add away_team_win float;

alter table Match drop constraint match_probability_id_fkey;

alter table Match add constraint match_probability_id_fkey foreign key (probability_id)
    references Probability(id) on delete cascade;