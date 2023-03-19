package de.ghse.forum.api;

import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
  final Logger logger = LoggerFactory.getLogger(AdminController.class);
  private final UserService userService;
  private final PostService postService;

  @DeleteMapping(path = "/user/{id}")
  public void deleteUser(@PathVariable("id") UUID id) {
    logger.info("Deleting user with id: " + id);
    userService.deleteUser(id);
  }

  @DeleteMapping(path = "/post/{id}")
  public void deletePost(@PathVariable("id") UUID id) {
    logger.info("Deleting post with id: " + id);
    postService.deletePost(id);
  }
}
