package com.branzmor.scoreboard;

import com.branzmor.scoreboard.properties.ScoreBoardProperties;
import cucumber.api.cli.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({ScoreBoardProperties.class})
public class MainApplication {
  public static void main(String[] args) {
    SpringApplication.run(Main.class);
  }
}

