package de.ghse.forum.api;

import de.ghse.forum.api.response.UserResponse;
import de.ghse.forum.service.UserService;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/v1/user")
@RestController
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

  @GetMapping(path = "test")
  public String test(Principal principal) {
    return principal.getName();
  }
}
