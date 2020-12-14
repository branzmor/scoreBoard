package com.branzmor.scoreboard.services;

import com.branzmor.scoreboard.repository.match.MatchEntity;
import com.branzmor.scoreboard.repository.match.MatchRepository;
import com.branzmor.scoreboard.repository.score.ScoreEntity;
import com.branzmor.scoreboard.repository.score.ScoreRepository;
import com.branzmor.scoreboard.repository.team.TeamEntity;
import com.branzmor.scoreboard.repository.team.TeamRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
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
        .lastEventTime(LocalDateTime.now())
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
    scoreRepository.save(score);
    match.setScore(score);
    matchRepository.save(match);
  }

  @Transactional
  public List<MatchEntity> getSummary() {

    List<MatchEntity> activeMatches = matchRepository.findByActive(true);
    printScoreBoard("DB", activeMatches);
    sortMatches(activeMatches);
    printScoreBoard("Sorted", activeMatches);
    return activeMatches;
  }

  private void sortMatches(List<MatchEntity> activeMatches) {
    activeMatches.sort(new Comparator<MatchEntity>() {
      public int compare(MatchEntity o1, MatchEntity o2) {
        return o1.getScore().getTotalGoals() > o2.getScore().getTotalGoals() ? -1 :
            o1.getScore().getTotalGoals() < o2.getScore().getTotalGoals() ? 1 : doSortByAdded(o1, o2);
      }

      public int doSortByAdded(MatchEntity o1, MatchEntity o2) {
        return o1.getLastEventTime().isAfter(o2.getLastEventTime()) ? -1 : o1.getLastEventTime().isBefore(o2.getLastEventTime()) ? 1 : 0;

      }
    });
  }

  public void printScoreBoard(String title,List<MatchEntity> activeMatches) {
    log.debug("---- {} -----", title);
    activeMatches.forEach(match -> {
      log.debug("{}-{} {}:{} ({})", match.getHomeTeam().getName(),match.getAwayTeam().getName(),
          match.getScore().getHomeTeamGoals(),match.getScore().getAwayTeamGoals(),match.getScore().getTotalGoals());
    });
    log.debug("---------");
  }
}
