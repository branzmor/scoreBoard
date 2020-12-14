package com.branzmor.scoreboard.services;

import com.branzmor.scoreboard.repository.match.MatchEntity;
import com.branzmor.scoreboard.repository.match.MatchRepository;
import com.branzmor.scoreboard.repository.score.ScoreEntity;
import com.branzmor.scoreboard.repository.score.ScoreRepository;
import com.branzmor.scoreboard.repository.team.TeamEntity;
import com.branzmor.scoreboard.repository.team.TeamRepository;
import com.branzmor.scoreboard.utils.MatchUtils;
import com.branzmor.scoreboard.utils.ScoreUtils;
import com.branzmor.scoreboard.utils.TeamUtils;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScoreBoardServiceTest {

  protected static final TeamEntity homeTeam = TeamUtils.generateRandomTeam();
  protected static final TeamEntity awayTeam = TeamUtils.generateRandomTeam();
  protected static final ScoreEntity score = ScoreUtils.generateRandomScore();
  protected static final MatchEntity match = MatchEntity.builder()
      .id(UUID.randomUUID().toString())
      .homeTeam(homeTeam)
      .awayTeam(awayTeam)
      .score(score)
      .active(true)
      .build();

  @Mock
  protected TeamRepository teamRepository;
  @Mock
  protected ScoreRepository scoreRepository;
  @Mock
  protected MatchRepository matchRepository;
  @InjectMocks
  protected ScoreBoardService sut;

  @Test
  @SneakyThrows
  public void startGame() {
    given(teamRepository.save(any(TeamEntity.class))).willReturn(new TeamEntity());
    given(scoreRepository.save(any(ScoreEntity.class))).willReturn(score);
    given(matchRepository.save(any(MatchEntity.class))).willReturn(match);

    sut.startGame(homeTeam, awayTeam);

    verify(teamRepository).save(homeTeam);
    verify(teamRepository).save(awayTeam);
    verify(scoreRepository).save(any(ScoreEntity.class));
    verify(matchRepository).save(any(MatchEntity.class));
  }

  @Test
  public void finishGame() {
    sut.finishGame(match);
    verify(matchRepository).save(any(MatchEntity.class));
  }

  @Test
  public void updateScore() {
    sut.updateScore(match, score);
    verify(scoreRepository).save(any(ScoreEntity.class));
    verify(matchRepository).save(any(MatchEntity.class));
  }

  @Test
  public void getSummary() {
    MatchEntity match1 = MatchUtils.generateMatch();
    MatchEntity match2 = MatchUtils.generateMatch();

    given(matchRepository.findByActive(true)).willReturn(Arrays.asList(match2, match1));

    List<MatchEntity> summary = sut.getSummary();

    assertEquals("Summary size does not match the expected one", 2, summary.size());
  }
}