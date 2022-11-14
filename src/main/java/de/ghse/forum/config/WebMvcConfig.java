package de.ghse.forum.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api").allowedOrigins("http://localhost:3000").allowedMethods("GET", "POST", "PUT", "DELETE").allowCredentials(true);
  }
}

