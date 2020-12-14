package com.branzmor.scoreboard.services;

import com.branzmor.scoreboard.MainApplication;
import com.branzmor.scoreboard.repository.match.MatchEntity;
import com.branzmor.scoreboard.repository.match.MatchRepository;
import com.branzmor.scoreboard.repository.score.ScoreEntity;
import com.branzmor.scoreboard.repository.score.ScoreRepository;
import com.branzmor.scoreboard.repository.team.TeamEntity;
import com.branzmor.scoreboard.repository.team.TeamRepository;
import com.branzmor.scoreboard.utils.TeamUtils;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MainApplication.class})
@DirtiesContext
@TestPropertySource(locations = "classpath:/application-test.properties")
public class ScoreBoardServiceItTest {

  @Autowired
  protected TeamRepository teamRepository;
  @Autowired
  protected ScoreRepository scoreRepository;
  @Autowired
  protected MatchRepository matchRepository;
  @Autowired
  protected ScoreBoardService scoreBoardService;

  @Before
  public void setUp() {
    matchRepository.deleteAll();
    scoreRepository.deleteAll();
    teamRepository.deleteAll();
  }

  @Test
  @SneakyThrows
  public void testStartGame() {

    TeamEntity homeTeam = TeamUtils.generateTeamWithName("Mexico");
    TeamEntity awayTeam = TeamUtils.generateTeamWithName("Canada");

    scoreBoardService.startGame(homeTeam, awayTeam);

    assertEquals(2, teamRepository.findAll().size());
    assertEquals(1, scoreRepository.findAll().size());
    assertEquals(1, matchRepository.findAll().size());
  }

  @Test
  public void testFinishGame() {
    TeamEntity homeTeam = TeamUtils.generateTeamWithName("Mexico");
    TeamEntity awayTeam = TeamUtils.generateTeamWithName("Canada");
    scoreBoardService.startGame(homeTeam, awayTeam);

    MatchEntity matchEntity = matchRepository.findAll().get(0);
    scoreBoardService.finishGame(matchEntity);

    assertTrue(matchRepository.findById(matchEntity.getId()).isPresent());
    assertFalse(matchRepository.findById(matchEntity.getId()).get().isActive());
  }

  @Test
  public void testUpdateScore() {
    TeamEntity homeTeam = TeamUtils.generateTeamWithName("Mexico");
    TeamEntity awayTeam = TeamUtils.generateTeamWithName("Canada");
    scoreBoardService.startGame(homeTeam, awayTeam);

    ScoreEntity scoreEntity = scoreRepository.findAll().get(0);
    MatchEntity matchEntity = matchRepository.findAll().get(0);

    scoreEntity.setAwayTeamGoals(5);
    scoreBoardService.updateScore(matchEntity, scoreEntity);

    assertTrue(scoreRepository.findById(scoreEntity.getId()).isPresent());
    assertTrue(matchRepository.findById(matchEntity.getId()).isPresent());
    assertEquals(5, scoreRepository.findById(scoreEntity.getId()).get().getAwayTeamGoals());
    assertEquals(5, matchRepository.findById(matchEntity.getId()).get().getScore().getAwayTeamGoals());
  }

  @Test
  public void testSummary() {
    TeamEntity homeTeam1 = TeamUtils.generateTeamWithName("Mexico");
    TeamEntity awayTeam1 = TeamUtils.generateTeamWithName("Canada");
    scoreBoardService.startGame(homeTeam1, awayTeam1);
    ScoreEntity scoreEntity1 = scoreRepository.findAll().get(0);
    scoreEntity1.setAwayTeamGoals(5);
    MatchEntity matchEntity1 = matchRepository.findAll().get(0);
    scoreBoardService.updateScore(matchEntity1, scoreEntity1);

    TeamEntity homeTeam2 = TeamUtils.generateTeamWithName("Spain");
    TeamEntity awayTeam2 = TeamUtils.generateTeamWithName("Brazil");
    scoreBoardService.startGame(homeTeam2, awayTeam2);
    ScoreEntity scoreEntity2 = scoreRepository.findAll().get(1);
    scoreEntity2.setHomeTeamGoals(10);
    scoreEntity2.setAwayTeamGoals(2);
    MatchEntity matchEntity2 = matchRepository.findAll().get(1);
    scoreBoardService.updateScore(matchEntity2, scoreEntity2);

    TeamEntity homeTeam3 = TeamUtils.generateTeamWithName("Uruguay");
    TeamEntity awayTeam3 = TeamUtils.generateTeamWithName("Italy");
    scoreBoardService.startGame(homeTeam3, awayTeam3);
    ScoreEntity scoreEntity3 = scoreRepository.findAll().get(2);
    scoreEntity3.setHomeTeamGoals(6);
    scoreEntity3.setAwayTeamGoals(6);
    MatchEntity matchEntity3 = matchRepository.findAll().get(2);
    scoreBoardService.updateScore(matchEntity3, scoreEntity3);

    List<MatchEntity> summary = scoreBoardService.getSummary();

    assertEquals("Summary size does not match the expected one", 3, summary.size());
    assertEquals("No expected match at this position", matchEntity3, summary.get(0));
    assertEquals("No expected match at this position", matchEntity2, summary.get(1));
    assertEquals("No expected match at this position", matchEntity1, summary.get(2));
  }
}
