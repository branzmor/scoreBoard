package com.branzmor.scoreboard.repository.score;

import com.branzmor.scoreboard.repository.team.TeamEntity;
import com.branzmor.scoreboard.utils.ScoreUtils;
import com.branzmor.scoreboard.utils.TeamUtils;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:/application-test.properties")
public class ScoreRepositoryTest {

  @Autowired
  private ScoreRepository sut;

  @Before
  public void before() {
    sut.deleteAll();
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void testPersistWithEntityNull() { sut.saveAndFlush(null); }

  @Test
  public void testSaveScore() {
    ScoreEntity score = ScoreUtils.generateRandomScore();
    sut.saveAndFlush(score);

    Optional<ScoreEntity> target = sut.findById(score.getId());
    assertTrue("Can't find stored Score", target.isPresent());
    assertEquals("", score, target.get());
  }

  @Test(expected = JpaSystemException.class)
  public void testIdCantBeNull() {
    ScoreEntity score = ScoreUtils.generateRandomScore();
    score.setId(null);
    sut.saveAndFlush(score);
  }

  @Test
  public void testIdCantBeEmpty() {
    ScoreEntity score = ScoreUtils.generateRandomScore();
    score.setId(StringUtils.EMPTY);
    sut.saveAndFlush(score);
  }

  @Test
  public void testStoreDifferentScores() {
    assertTrue("DB should be empty at this point", sut.findAll().isEmpty());

    ScoreEntity score = ScoreUtils.generateRandomScore();
    sut.saveAndFlush(score);

    score.setId(UUID.randomUUID().toString());
    sut.saveAndFlush(score);

    List<ScoreEntity> entities = sut.findAll();
    assertEquals("We stored 2 different entities, but we read " + entities.size(), 2, sut.findAll().size());

  }


}
