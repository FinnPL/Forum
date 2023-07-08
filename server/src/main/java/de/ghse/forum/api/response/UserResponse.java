package de.ghse.forum.api.response;

import de.ghse.forum.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON model returned after fetching a User.
 *
 * @see de.ghse.forum.api.UserController UserController
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private String user_name;
  private UUID id;
  private String bio;
  private Role role;

  /**
   * Converts a List of User objects to a List of UserResponse objects.
   *
   * @param allUsers List of User objects.
   * @return List of UserResponse objects.
   */
  public Iterable<UserResponse> convert(Iterable<User> allUsers) {
    List<UserResponse> userResponses = new ArrayList<>();
    for (User user : allUsers) {
      userResponses.add(convert(user));
    }
    return userResponses;
  }

  /**
   * Converts a User object to a UserResponse object.
   *
   * @param user User object.
   * @return UserResponse object.
   */
  public UserResponse convert(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .user_name(user.getUsername())
        .bio(user.getBio())
        .role(user.getRole())
        .build();
  }
}
