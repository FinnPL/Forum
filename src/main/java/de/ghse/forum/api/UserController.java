package de.ghse.forum.api;

import de.ghse.forum.model.User;
import de.ghse.forum.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 *
 *
 * <pre>
 * User Controller
 * Location's under: <a href="http://localhost:8080/api/v1/user/">/user</a>
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
@RequestMapping("api/v1/user")
@RestController
public class UserController {

  private final UserService userService;

  /**
   * Autowired Constructor
   *
   * @param userService User Service
   * @see UserService
   * @since 1.0
   */
  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }


  @PostMapping
  public void addUser(@Valid @NonNull @RequestBody UserRequest userRequest) {
    User user = new User();
    user.setUsername(userRequest.getUsername());
    userService.addUser(user);
  }

  @GetMapping(path = "{id}")
  public UserResponse getUser(@PathVariable("id") UUID id) {
    return new UserResponse().convert(userService.findUserById(id).orElseThrow());
  }
  @GetMapping(path = "search/{query}/{page}")
  public Iterable<UserResponse> search(@PathVariable("query") String query, @PathVariable("page") int page) {
    return new UserResponse().convert(userService.search(query, page));
  }

  // Response and Request Classes:
  // *************************************************************************************************************************************

  /**
   *
   *
   * <pre>
   * UserResponse Class for API Responses
   * </pre>
   *
   * @see UserController
   * @see User
   * @since 1.0
   */
  @Data
  public static class UserResponse {
    private String username;
    private UUID id;

    /**
     *
     *
     * <pre>
     * Convert List of Users to UserResponse
     * </pre>
     *
     * @param allUsers List of Users to convert to UserResponses
     * @return UserResponse List of UserResponses
     * @see UserResponse
     * @see User
     * @since 1.0
     */
    public Iterable<UserResponse> convert(Iterable<User> allUsers) {
      List<UserResponse> userResponses = new ArrayList<>();
      for (User user : allUsers) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setId(user.getId());
        userResponses.add(userResponse);
      }
      return userResponses;
    }

    /**
     * Convert User to UserResponse
     *
     * @param user User to convert
     * @return UserResponse
     * @see User
     * @see UserResponse
     * @since 1.0
     */
    public UserResponse convert(User user) {
      UserResponse userResponse = new UserResponse();
      userResponse.setUsername(user.getUsername());
      userResponse.setId(user.getId());
      return userResponse;
    }
  }

  /**
   *
   *
   * <pre>
   * UserRequest Class for API Requests
   * </pre>
   *
   * @see UserController
   * @see User
   * @since 1.0
   */
  @Data
  public static class UserRequest {
    private String username;
  }
}
