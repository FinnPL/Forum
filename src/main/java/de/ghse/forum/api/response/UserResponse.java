package de.ghse.forum.api.response;

import de.ghse.forum.api.UserController;
import de.ghse.forum.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;

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
public class UserResponse {
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
