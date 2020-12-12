package com.branzmor.scoreboard.services;

import com.branzmor.scoreboard.repository.score.ScoreEntity;
import com.branzmor.scoreboard.repository.match.MatchEntity;
import com.branzmor.scoreboard.repository.match.MatchRepository;
import com.branzmor.scoreboard.repository.team.TeamEntity;
import com.branzmor.scoreboard.repository.team.TeamRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ScoreBoardService {

  protected TeamRepository teamRepository;
  protected MatchRepository matchRepository;

  public void startGame(TeamEntity homeTeam, TeamEntity awayTeam) {
    //TODO: Creates a new match with those

    //TODO: Add the match to the scoreboard
  }

  public void finishGame(TeamEntity homeTeam, TeamEntity awayTeam) {
    //TODO: Remove match from the scoreboard
  }

  public void updateScore(MatchEntity matchEntity, ScoreEntity socreEntity) {
    //TODO: Receiving the pair score; home team score and away team score udpdates a game score
  }

  public List<MatchEntity> getSummary() {
    //TODO: Get a summaary of games by total score.
    // Those games with the same total score will be returned ordered by the most recently added to our system
    return null;
  }
}
