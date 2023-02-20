package de.ghse.forum.api;

import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class AdminController {
  private final UserService userService;
  private final PostService postService;

  @DeleteMapping(path = "/user/{id}")
  public void deleteUser(@PathVariable("id") UUID id) {
    userService.deleteUser(id);
  }

  @DeleteMapping(path = "/post/{id}")
  public void deletePost(@PathVariable("id") UUID id) {
    postService.deletePost(id);
  }
}
