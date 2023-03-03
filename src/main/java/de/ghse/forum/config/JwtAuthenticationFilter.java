package de.ghse.forum.config;

import de.ghse.forum.service.JwtService;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
     logger.info(
              "Request: "
                      + request.getRequestURI()
                      + " Header Auth: "
                      + request.getHeader("Authorization")
                      + " Host: "
                      + request.getHeader("Host")
                      + " Origin: "
                      + request.getHeader("Origin")
                      + " Referer: "
                      + request.getHeader("Referer")
                      + " User-Agent: "
                      + request.getHeader("User-Agent")
                      + " Accept: "
                      + request.getHeader("Accept")
                      + " Accept-Encoding: "
                      + request.getHeader("Accept-Encoding")
                      + " Accept-Language: "
                      + request.getHeader("Accept-Language")
                      + " Connection: "
                      + request.getHeader("Connection")
                      + " Cookie: "
                      + request.getHeader("Cookie")
                      + " Sec-Fetch-Dest: "
                      + request.getHeader("Sec-Fetch-Dest"));

      final String authorizationHeader = request.getHeader("Authorization");
      final String jwt;
      final String username;
      if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      } else {
        jwt = authorizationHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
          if (jwtService.isTokenValid(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
          }
        }
        filterChain.doFilter(request, response);
      }
  }
}
