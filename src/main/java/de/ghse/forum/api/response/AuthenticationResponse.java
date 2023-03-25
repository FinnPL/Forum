package de.ghse.forum.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON model returned after authentication of a User.
 *
 * @see de.ghse.forum.api.AuthenticationController AuthenticationController
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
  private String token;
  private String user_id;
}
