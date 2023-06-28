package de.ghse.forum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entity Model for users.
 *
 * @apiNote This model is mapped to the database table "user".
 * @see Post Post
 * @see Comment Comment
 * @see de.ghse.forum.service.UserService UserService
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", columnDefinition = "UUID")
  private UUID id;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Collection<Post> posts;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Collection<Comment> comments;

  @Column(unique = true)
  private String username;

  @NotBlank private String password;
  private String bio;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
