package de.ghse.forum;

import de.ghse.forum.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class ForumApplication {

  public static void main(String[] args) {
    SpringApplication.run(ForumApplication.class, args);
  }
}
