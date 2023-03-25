package de.ghse.forum.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON model used for authentication of a User.
 *
 * @apiNote JSON body of the POST /api/v1/auth/authenticate endpoint.
 * @see de.ghse.forum.api.AuthenticationController#authentication(AuthenticationRequest)
 *     AuthenticationController
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
  private String user_name;
  private String password;
}
