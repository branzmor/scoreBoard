package com.branzmor.scoreboard.repository.team;

import com.branzmor.scoreboard.utils.TeamUtils;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintViolationException;
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
public class TeamRepositoryTest {

  @Autowired
  private TeamRepository sut;

  @Before
  public void before() {
    sut.deleteAll();
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void testPersistWithEntityNull() {
    sut.saveAndFlush(null);
  }

  @Test
  public void testSaveTeam() {
    TeamEntity team = TeamUtils.generateRandomTeam();
    sut.saveAndFlush(team);

    Optional<TeamEntity> target = sut.findById(team.getId());
    assertTrue("Can't find stored Team", target.isPresent());
    assertEquals("", team, target.get());
  }

  @Test(expected = JpaSystemException.class)
  public void testIdCantBeNull() {
    TeamEntity team = TeamUtils.generateRandomTeam();
    team.setId(null);
    sut.saveAndFlush(team);
  }

  @Test
  public void testIdCantBeEmpty() {
    TeamEntity team = TeamUtils.generateRandomTeam();
    team.setId(StringUtils.EMPTY);
    sut.saveAndFlush(team);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testNameCantBeNull() {
    TeamEntity team = TeamUtils.generateRandomTeam();
    team.setName(null);
    sut.saveAndFlush(team);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testNameCantBeEmpty() {
    TeamEntity team = TeamUtils.generateRandomTeam();
    team.setName(StringUtils.EMPTY);
    sut.saveAndFlush(team);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testNameCantBeBlank() {
    TeamEntity team = TeamUtils.generateRandomTeam();
    team.setName(StringUtils.SPACE);
    sut.saveAndFlush(team);
  }

  @Test
  public void testStoreDifferentTeams() {
    assertTrue("DB should be empty at this point", sut.findAll().isEmpty());

    TeamEntity team = TeamUtils.generateRandomTeam();
    sut.saveAndFlush(team);

    team.setId(UUID.randomUUID().toString());
    sut.saveAndFlush(team);

    List<TeamEntity> entities = sut.findAll();
    assertEquals("We stored 2 different entities, but we read " + entities.size(), 2, sut.findAll().size());

  }

}


