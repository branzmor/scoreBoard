package com.branzmor.scoreboard.repository.team;

import com.branzmor.scoreboard.repository.AuditableEntity;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "team")
public class TeamEntity extends AuditableEntity {

  @Id
  @NotNull
  private String id;

  @NotBlank
  private String name;

}
