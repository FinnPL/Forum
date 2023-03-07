package de.ghse.forum.api;

import de.ghse.forum.api.response.UserResponse;
import de.ghse.forum.service.UserService;

import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/v1/user")
@RestController
public class UserController {

  private final UserService userService;
  Logger logger = LoggerFactory.getLogger(UserController.class);

  @GetMapping(path = "{id}")
  public ResponseEntity<UserResponse> getUser(@PathVariable("id") UUID id) {
    try {
      return ResponseEntity.ok()
          .body(new UserResponse().convert(userService.findUserById(id).orElseThrow()));
    } catch (Exception e) {
      logger.error("User with id: " + id + " not found");
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(path = "search/{query}/{page}")
  public Iterable<UserResponse> search(
      @PathVariable("query") String query, @PathVariable("page") int page) {
    return new UserResponse().convert(userService.search(query, page));
  }

  @PutMapping(path = "update/{Bio}")
  public ResponseEntity<UserResponse> updateUser(@PathVariable("Bio") String bio, Principal principal) {
      userService.updateUser(bio, userService.findbyUsername(principal.getName()).orElseThrow().getId());
      return ResponseEntity.ok().build();
  }
}
