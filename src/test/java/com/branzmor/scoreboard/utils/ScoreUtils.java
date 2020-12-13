package com.branzmor.scoreboard.utils;

import com.branzmor.scoreboard.repository.score.ScoreEntity;
import java.util.UUID;

public class ScoreUtils {

  public static ScoreEntity generateRandomScore() {
    return ScoreEntity.builder()
        .id(UUID.randomUUID().toString())
        .homeTeamGoals(0)
        .awayTeamGoals(0)
        .build();
  }
}
