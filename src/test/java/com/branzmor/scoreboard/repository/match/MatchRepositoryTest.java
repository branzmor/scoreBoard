package com.branzmor.scoreboard.repository.match;

import com.branzmor.scoreboard.repository.score.ScoreEntity;
import com.branzmor.scoreboard.repository.score.ScoreRepository;
import com.branzmor.scoreboard.repository.team.TeamEntity;
import com.branzmor.scoreboard.repository.team.TeamRepository;
import com.branzmor.scoreboard.utils.MatchUtils;
import com.branzmor.scoreboard.utils.ScoreUtils;
import com.branzmor.scoreboard.utils.TeamUtils;
import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:/application-test.properties")
public class MatchRepositoryTest {

  @Autowired
  private TeamRepository teamRepository;

  @Autowired
  private ScoreRepository scoreRepository;

  @Autowired
  private MatchRepository sut;

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void testPersistWithEntityNull() {
    sut.saveAndFlush(null);
  }

  @Test
  public void testSaveMatch() {
    MatchEntity match = getMatch();
    sut.saveAndFlush(match);

    Optional<MatchEntity> target = sut.findById(match.getId());
    assertTrue("Can't find stored Team", target.isPresent());
    assertEquals("Id does not match", match.getId(), target.get().getId());
  }

  @Test(expected = JpaObjectRetrievalFailureException.class)
  public void testForeignKeyViolatedForHomeTeam() {
    TeamEntity awayTeam = TeamUtils.generateRandomTeam();
    teamRepository.saveAndFlush(awayTeam);

    ScoreEntity score = ScoreUtils.generateRandomScore();
    scoreRepository.saveAndFlush(score);

    MatchEntity match = MatchUtils.generateMatch(TeamUtils.generateRandomTeam(),awayTeam,score);
    sut.saveAndFlush(match);
  }

  @Test(expected = JpaObjectRetrievalFailureException.class)
  public void testForeignKeyViolatedForAwayTeam() {
    TeamEntity homeTeam = TeamUtils.generateRandomTeam();
    teamRepository.saveAndFlush(homeTeam);

    ScoreEntity score = ScoreUtils.generateRandomScore();
    scoreRepository.saveAndFlush(score);

    MatchEntity match = MatchUtils.generateMatch(homeTeam, TeamUtils.generateRandomTeam(),score);
    sut.saveAndFlush(match);
  }

  @Test(expected = JpaObjectRetrievalFailureException.class)
  public void testForeignKeyViolatedForScore() {
    TeamEntity homeTeam = TeamUtils.generateRandomTeam();
    teamRepository.saveAndFlush(homeTeam);

    TeamEntity awayTeam = TeamUtils.generateRandomTeam();
    teamRepository.saveAndFlush(awayTeam);

    MatchEntity match = MatchUtils.generateMatch(homeTeam, awayTeam, ScoreUtils.generateRandomScore());
    sut.saveAndFlush(match);
  }

  @Test(expected = JpaSystemException.class)
  public void testIdCantBeNull() {
    MatchEntity match = getMatch();
    match.setId(null);
    sut.saveAndFlush(match);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testIdCantBeEmpty() {
    MatchEntity match = getMatch();
    match.setId(StringUtils.EMPTY);
    sut.saveAndFlush(match);
  }

  @Test
  public void testSaveTwoDifferentEntitiesWithSameId() {
    MatchEntity matchEntity = getMatch();
    sut.saveAndFlush(matchEntity);

    TeamEntity awayTeam = TeamUtils.generateRandomTeam();
    teamRepository.saveAndFlush(awayTeam);
    matchEntity.setAwayTeam(awayTeam);
    sut.saveAndFlush(matchEntity);

    List<MatchEntity> target = sut.findAll();
    assertEquals(1, target.size());
  }

  private MatchEntity getMatch() {
    TeamEntity homeTeam = TeamUtils.generateRandomTeam();
    teamRepository.saveAndFlush(homeTeam);
    TeamEntity awayTeam = TeamUtils.generateRandomTeam();
    teamRepository.saveAndFlush(awayTeam);

    ScoreEntity score = ScoreUtils.generateRandomScore();
    scoreRepository.saveAndFlush(score);

    return MatchUtils.generateMatch(homeTeam,awayTeam,score);
  }
}
