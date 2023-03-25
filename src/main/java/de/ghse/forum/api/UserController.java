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

/**
 * REST controller for user related endpoints.
 * @apiNote This controller is accessible under /api/v1/user.
 * @see UserService
 * @see de.ghse.forum.model.User User
 */
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
@RestController
public class UserController {

  private final UserService userService;
  final Logger logger = LoggerFactory.getLogger(UserController.class);

  /**
   * REST endpoint for getting a user by id.
   * @apiNote This endpoint is accessible under /api/v1/user/{id}.
   * @param id the UUID of the user to be returned
   * @return the user with the given id
   * @see de.ghse.forum.model.User User
   * @see UserResponse
   */
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

  /**
   * REST endpoint searching for a user by username.
   * @apiNote This endpoint is accessible under /api/v1/user/search/{query}/{page}.
   * @param query the username to be searched for
   * @return the user with the given username
   * @see de.ghse.forum.model.User User
   * @see UserResponse
   */
  @GetMapping(path = "search/{query}/{page}")
  public Iterable<UserResponse> search(
      @PathVariable("query") String query, @PathVariable("page") int page) {
    return new UserResponse().convert(userService.search(query, page));
  }

  /**
   * REST endpoint for updating a user.
   * @apiNote This endpoint is accessible under /api/v1/user/update/{Bio}.
   * @param bio the new bio of the user
   * @param principal the user to be updated (Spring internal)
   * @return the updated user
   * @see de.ghse.forum.model.User User
   * @see UserResponse
   */
  @PutMapping(path = "update/{Bio}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable("Bio") String bio, Principal principal) {
    userService.updateUser(
        bio, userService.findbyUsername(principal.getName()).orElseThrow().getId());
    return ResponseEntity.ok().build();
  }
}
