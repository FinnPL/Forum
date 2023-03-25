package de.ghse.forum.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * JSON model used for registering a new User.
 * @apiNote Request body of the POST /api/v1/auth/register endpoint.
 * @see de.ghse.forum.api.AuthenticationController#register(RegisterRequest) AuthenticationController
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  private String user_name;
  private String password;
}
