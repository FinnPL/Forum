package de.ghse.forum.service;

import de.ghse.forum.api.request.AuthenticationRequest;
import de.ghse.forum.api.request.RegisterRequest;
import de.ghse.forum.api.response.AuthenticationResponse;
import de.ghse.forum.model.Role;
import de.ghse.forum.model.User;
import de.ghse.forum.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for authentication.
 *
 * @apiNote This service is used by the AuthenticationController.
 * @see de.ghse.forum.api.AuthenticationController AuthenticationController
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  /**
   * Generates a JWT token for the user.
   *
   * @param request AuthenticationRequest
   * @return AuthenticationResponse with JWT token and user id
   */
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUser_name(), request.getPassword()));
    var user = userRepository.findByUsername(request.getUser_name()).orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .user_id(user.getId().toString())
        .role(user.getRole())
        .build();
  }

  /**
   * Verifies the signature of the data. Creates a new user or update the password if the user
   * already exists.
   *
   * @param request RegisterRequest
   * @return AuthenticationResponse with JWT token and user id
   * @throws Exception if the signature is invalid
   */
  public AuthenticationResponse noeAuth(RegisterRequest request) throws Exception {
    String signature = request.getSignature();
    String publicKeyString =
        "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAEaRpPYjAH0DjaNPwQSLrLmuz+deOLA4RBxTV/t0DV0zXWlYB+Ifaa6wE5QgikFs64PpHWhssiT+fdMccA4dUQPix35P8yGbccYvmUdm96WeITfgTHFSy/46vfwTm305UK";
    String data =
        "{\"givenname\":\""
            + request.getGivenname()
            + "\",\"surname\":\""
            + request.getSurname()
            + "\",\"class\":\""
            + request.getClassname()
            + "\",\"login\":\""
            + request.getUser_name()
            + "\",\"timestamp\":"
            + request.getTimestamp()
            + "}"; // Dont ask 💀
    if (verifyData(data, signature, publicKeyString)) {
      System.out.println(System.currentTimeMillis());
      if (System.currentTimeMillis() - Long.parseLong(request.getTimestamp()) * 1000 > 300000) {
        throw new Exception("Timestamp is not valid");
      }
      if (userRepository.findByUsername(request.getUser_name()).isPresent()) {
        User user = userRepository.findByUsername(request.getUser_name()).get();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
      } else {
        User user =
            User.builder()
                .username(request.getUser_name())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .username(request.getUser_name())
                .build();
        userRepository.save(user);
      }
      return authenticate(
          AuthenticationRequest.builder()
              .user_name(request.getUser_name())
              .password(request.getPassword())
              .build());
    } else {
      throw new Exception("Signature is not valid");
    }
  }

  /**
   * Verifies Data with a signature and a public key.
   *
   * @param data JSON data
   * @param signature Signature of the data
   * @param publicKeyString Public key
   * @return true if the signature is valid
   */
  private boolean verifyData(String data, String signature, String publicKeyString) {
    try {
      byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("EC");
      PublicKey publicKey = keyFactory.generatePublic(keySpec);

      Signature sig = Signature.getInstance("SHA256withECDSA");
      sig.initVerify(publicKey);
      sig.update(data.getBytes(StandardCharsets.UTF_8));

      byte[] signatureBytes = Base64.getDecoder().decode(signature);
      return sig.verify(signatureBytes);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
