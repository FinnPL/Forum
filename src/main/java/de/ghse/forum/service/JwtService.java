package de.ghse.forum.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service for JWT.
 *
 * @apiNote This service is used to generate and validate JWT tokens.
 * @see de.ghse.forum.config.JwtAuthenticationFilter JwtAuthenticationFilter
 */
@Service
public class JwtService {

  private static final String SECRET_KEY =
      "77397A24432646294A404D635166546A576E5A7234753778214125442A472D4B";

  /**
   * Extracts the username from the JWT token.
   *
   * @param token JWT token
   * @return username
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extracts a claim from the JWT token and applies a function to it.
   *
   * @param token JWT token
   * @param claimsResolver function to apply to the claim (e.g. Claims::getSubject)
   * @param <T> type of the claim
   * @return the extracted information
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    try {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
    } catch (Exception e) {
      throw new RuntimeException("Invalid token");
    }
  }

  /**
   * Generates a JWT token for the user.
   *
   * @param userDetails UserDetails
   * @return JWT token
   */
  public String generateToken(UserDetails userDetails) {
    return generateToken(Map.of(), userDetails);
  }

  /**
   * Generates a JWT token for the user with additional claims.
   *
   * @param extraClaims additional claims
   * @param userDetails UserDetails
   * @return JWT token
   */
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * Checks if the JWT token is valid.
   *
   * @param token JWT token
   * @param userDetails UserDetails
   * @return true if the token is valid, false otherwise
   */
  public Boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  /**
   * Checks if the JWT token is expired.
   *
   * @param token JWT token
   * @return true if the token is expired, false otherwise
   */
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extracts the expiration date from the JWT token.
   *
   * @param token JWT token
   * @return expiration date
   */
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts all claims from the JWT token.
   *
   * @param token JWT token
   * @return all claims
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Returns the secret key.
   *
   * @return secret key
   */
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
