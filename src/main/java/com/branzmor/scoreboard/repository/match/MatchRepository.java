package com.branzmor.scoreboard.repository.match;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, String> {
  List<MatchEntity> findByActive(boolean flag);
}
