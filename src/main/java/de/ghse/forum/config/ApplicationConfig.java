package de.ghse.forum.config;

import de.ghse.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ApplicationConfig is a Spring configuration class for the application.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final UserRepository userRepository;

  /**
   * Creates a UserDetailsService bean.
   * @return a UserDetailsService bean
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return username ->
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

    /**
     * Creates an AuthenticationProvider bean.
     * @return an AuthenticationProvider bean
     */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

    /**
     * Creates an AuthenticationManager bean.
     * @param config the AuthenticationConfiguration
     * @return an AuthenticationManager bean
     * @throws Exception if an error occurs
     */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

    /**
     * Creates a PasswordEncoder bean.
     * @return a PasswordEncoder bean
     */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
