package com.branzmor.scoreboard.utils;

import com.branzmor.scoreboard.repository.team.TeamEntity;
import java.util.UUID;

public class TeamUtils {

  public static TeamEntity generateTeamWithName(String name) {
    return TeamEntity.builder()
        .id(UUID.randomUUID().toString())
        .name(name)
        .build();
  }

  public static TeamEntity generateRandomTeam() {
    return generateTeamWithName(UUID.randomUUID().toString());
  }
}
