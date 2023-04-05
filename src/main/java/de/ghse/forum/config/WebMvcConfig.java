package de.ghse.forum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class WebMvcConfig {
  @Bean
  public CorsFilter corsFilter() throws UnknownHostException {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    System.out.println("http://"+ InetAddress.getLocalHost().getHostAddress());
    config.addAllowedOrigin("http://"+ InetAddress.getLocalHost().getHostAddress());
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
