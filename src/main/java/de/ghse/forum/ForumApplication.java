package de.ghse.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class ForumApplication {

  public static void main(String[] args) {
    SpringApplication.run(ForumApplication.class, args);
  }
}
