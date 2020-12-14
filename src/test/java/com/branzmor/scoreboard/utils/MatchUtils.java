package com.branzmor.scoreboard.utils;

import com.branzmor.scoreboard.repository.match.MatchEntity;
import com.branzmor.scoreboard.repository.score.ScoreEntity;
import com.branzmor.scoreboard.repository.team.TeamEntity;
import java.time.LocalDateTime;
import java.util.UUID;

public class MatchUtils {

  public static MatchEntity generateMatch(TeamEntity homeTeam, TeamEntity awayTeam, ScoreEntity score) {
    return MatchEntity.builder()
        .id(UUID.randomUUID().toString())
        .homeTeam(homeTeam)
        .awayTeam(awayTeam)
        .score(score)
        .lastEventTime(LocalDateTime.now())
        .active(true)
        .build();
  }

  public static MatchEntity generateMatch(TeamEntity homeTeam, TeamEntity awayTeam) {
    return MatchEntity.builder()
        .id(UUID.randomUUID().toString())
        .homeTeam(homeTeam)
        .awayTeam(awayTeam)
        .score(ScoreUtils.generateRandomScore())
        .lastEventTime(LocalDateTime.now())
        .active(true)
        .build();
  }
  public static MatchEntity generateMatch() {

    TeamEntity homeTeam = TeamUtils.generateRandomTeam();
    TeamEntity awayTeam = TeamUtils.generateRandomTeam();
    ScoreEntity score = ScoreUtils.generateRandomScore();

    return MatchEntity.builder()
        .id(UUID.randomUUID().toString())
        .homeTeam(homeTeam)
        .awayTeam(awayTeam)
        .score(score)
        .lastEventTime(LocalDateTime.now())
        .active(true)
        .build();
  }
}
