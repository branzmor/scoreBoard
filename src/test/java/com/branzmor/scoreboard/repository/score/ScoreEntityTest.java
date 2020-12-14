package com.branzmor.scoreboard.repository.score;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:/application-test.properties")
public class ScoreEntityTest{

  ScoreEntity score;

  @Before
  public void setUp() {
    score = ScoreEntity.builder().id("ScoreId").homeTeamGoals(3).awayTeamGoals(3).build();
  }

  @Test
  public void getTotalGoals() {
    assertEquals(6,score.getTotalGoals());
  }
}
