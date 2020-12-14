package com.branzmor.scoreboard.repository.match;

import com.branzmor.scoreboard.repository.AuditableEntity;
import com.branzmor.scoreboard.repository.score.ScoreEntity;
import com.branzmor.scoreboard.repository.team.TeamEntity;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "match")
public class MatchEntity extends AuditableEntity {

  @Id
  @NotBlank
  private String id;

  @ManyToOne
  @JoinColumn(name="home", referencedColumnName = "id")
  private TeamEntity homeTeam;

  @ManyToOne
  @JoinColumn(name="away", referencedColumnName = "id")
  private TeamEntity awayTeam;

  @ManyToOne
  @JoinColumn(name="score", referencedColumnName = "id")
  private ScoreEntity score;

  @LastModifiedDate
  private LocalDateTime lastEventTime;

  private boolean active;
}
