package de.ghse.forum.service;

import de.ghse.forum.model.User;
import de.ghse.forum.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 *
 * <pre>
 * User Service Class
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
@Service
public class UserService {
  @Autowired private UserRepository UserRepository;

  /**
   * Add User to Database
   *
   * @param user User to add
   * @see User
   * @see UserRepository
   * @since 1.0
   */
  public void addUser(User user) {
    UserRepository.save(user);
  }

  /**
   * Find User by ID
   *
   * @param id UUID of User
   * @return Optional User
   * @see User
   * @see UserRepository
   * @since 1.0
   */
  public @NotNull Optional<User> findUserById(UUID id) {
    return UserRepository.findById(id);
  }

  /**
   * Get all Users
   *
   * @return List of Users
   * @see User
   * @see UserRepository
   * @since 1.0
   * @deprecated Debugging only
   */
  public @NotNull Iterable<User> getAllUsers() {
    return UserRepository.findAll();
  }

  /**
   * Find User by Username Containing a String
   *
   * @param name String to search for in Username
   * @return List of Users
   * @see User
   * @see UserRepository
   * @since 1.0
   */
  public Iterable<User> getAllByUsernameContaining(String name) {
    return UserRepository.findAllByUsernameContaining(name);
  }

    /**
     * Find 20 Users by Username containing a String
     *
     * @param username Username to search for
     * @return list of User
     * @see User
     * @see UserRepository
     * @since 1.0
     */
    public List<User> find20ByUsernameContaining(String username) {
      return UserRepository.findTop20ByUsernameContainingIgnoreCaseOrderByUsernameDesc(username);
    }
}
