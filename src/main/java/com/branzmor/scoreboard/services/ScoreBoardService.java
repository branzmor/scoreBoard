package com.branzmor.scoreboard.services;

import com.branzmor.scoreboard.repository.score.ScoreEntity;
import com.branzmor.scoreboard.repository.match.MatchEntity;
import com.branzmor.scoreboard.repository.match.MatchRepository;
import com.branzmor.scoreboard.repository.score.ScoreRepository;
import com.branzmor.scoreboard.repository.team.TeamEntity;
import com.branzmor.scoreboard.repository.team.TeamRepository;
import gherkin.formatter.model.Match;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class ScoreBoardService {

  private final TeamRepository teamRepository;
  private final ScoreRepository scoreRepository;
  private final MatchRepository matchRepository;

  @Transactional
  public void startGame(TeamEntity homeTeam, TeamEntity awayTeam) {
    teamRepository.save(homeTeam);
    teamRepository.save(awayTeam);

    log.debug("Teams has been inserted in DB: {}", teamRepository.findAll());

    ScoreEntity score = ScoreEntity.builder()
        .id(homeTeam.getId() + "vs" + awayTeam.getId())
        .homeTeamGoals(0)
        .awayTeamGoals(0)
        .build();

    scoreRepository.save(score);
    log.debug("Score has been inserted in DB: {}", scoreRepository.findAll());

    MatchEntity match = MatchEntity.builder()
        .id(score.getId() + LocalDateTime.now())
        .homeTeam(homeTeam)
        .awayTeam(awayTeam)
        .score(score)
        .active(true)
        .build();

    matchRepository.save(match);
    log.debug("Match has been inserted in DB: {}", matchRepository.findAll());
  }

  @Transactional
  public void finishGame(MatchEntity match) {
    match.setActive(false);
    matchRepository.save(match);
  }

  @Transactional
  public void updateScore(MatchEntity match, ScoreEntity score) {
    match.setScore(score);
    matchRepository.save(match);
  }

  @Transactional
  public List<MatchEntity> getSummary() {
    //TODO: Get a summary of games by total score.
    // Those games with the same total score will be returned ordered by the most recently added to our system
    return matchRepository.findAll();
  }
}
