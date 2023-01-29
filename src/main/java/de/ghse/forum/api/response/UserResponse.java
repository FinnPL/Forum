package de.ghse.forum.api.response;

import de.ghse.forum.api.UserController;
import de.ghse.forum.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private String username;
  private UUID id;

  public Iterable<UserResponse> convert(Iterable<User> allUsers) {
    List<UserResponse> userResponses = new ArrayList<>();
    for (User user : allUsers) {
      userResponses.add(convert(user));
    }
    return userResponses;
  }

  public UserResponse convert(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .build();
  }
}
