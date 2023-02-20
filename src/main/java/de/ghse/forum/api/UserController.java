package de.ghse.forum.api;

import de.ghse.forum.api.response.UserResponse;
import de.ghse.forum.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/v1/user")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
public class UserController {

  private final UserService userService;

  @GetMapping(path = "{id}")
  public UserResponse getUser(@PathVariable("id") UUID id) {
    return new UserResponse().convert(userService.findUserById(id).orElseThrow());
  }

  @GetMapping(path = "search/{query}/{page}")
  public Iterable<UserResponse> search(
      @PathVariable("query") String query, @PathVariable("page") int page) {
    return new UserResponse().convert(userService.search(query, page));
  }
}
