package de.ghse.forum;

import de.ghse.forum.api.FileDataController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class ForumApplication {

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void main(String[] args) {
    if(!new File(FileDataController.directory).exists()) {
      new File(FileDataController.directory).mkdir();
    }
    SpringApplication.run(ForumApplication.class, args);
  }
}
