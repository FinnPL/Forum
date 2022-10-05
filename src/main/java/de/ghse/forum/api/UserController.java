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

  // debug Requests:
  // **************************************************************************************************************************************************

  /**
   *
   *
   * <pre>
   * Debug Request Only!
   * Api Post Request to create a new User
   * Location: <a href="http://localhost:8080/api/v1/user">/</a>
   * </pre>
   *
   * @param userRequest User to create
   * @see UserRequest
   * @see UserService#addUser(User)
   * @deprecated Debug Request Only!
   * @since 1.0
   */
  @PostMapping
  public void addUser(@Valid @NonNull @RequestBody UserRequest userRequest) {
    User user = new User();
    user.setUsername(userRequest.getUsername());
    userService.addUser(user);
  }

  /**
   *
   *
   * <pre>
   * Debug Request Only!
   * Get all Users from Database
   * Location: <a href="http://localhost:8080/api/v1/user/all">/all</a>
   * </pre>
   *
   * @return List of all Users
   * @deprecated Debug Request Only!
   * @since 1.0
   */
  @GetMapping(path = "/all")
  public Iterable<UserResponse> getAllUsers() {
    return new UserResponse().convert(userService.getAllUsers());
  }

  // API Requests:
  // ****************************************************************************************************************************************************

  /**
   *
   *
   * <pre>
   * Api Get Request to get a User by Id
   * Location: <a href="http://localhost:8080/api/v1/user/{id}">/{id}</a>
   * </pre>
   *
   * @param id id of the User
   * @return UserResponse
   * @see UserResponse
   * @see UserService#findUserById(UUID)
   * @since 1.0
   */
  @GetMapping(path = "{id}")
  public UserResponse getUser(@PathVariable("id") UUID id) {
    return new UserResponse().convert(userService.findUserById(id).orElseThrow());
  }

  /**
   *
   *
   * <pre>
   * Api Get Request to get a User by Username containing String
   * Location: <a href="http://localhost:8080/api/v1/user/search/{username}">/search/{username}</a>
   * </pre>
   *
   * @param name search query as String
   * @return Iterable UserResponse
   * @see UserResponse
   * @see UserService#getAllByUsernameContaining(String)
   * @since 1.0
   */
  @GetMapping(path = "/search/{name}")
  public Iterable<UserResponse> getAllByUsernameContaining(@PathVariable("name") String name) {
    return new UserResponse().convert(userService.getAllByUsernameContaining(name));
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
