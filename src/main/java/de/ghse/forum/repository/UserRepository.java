package de.ghse.forum.repository;

import de.ghse.forum.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * <pre>
 * User Repository Interface extends JpaRepository
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   *
   *
   * <pre>
   * Find User by ID
   * </pre>
   *
   * @param uuid ID of User
   * @return Optional of User
   * @see User
   * @since 1.0
   */
  @NotNull
  Optional<User> findById(@NotNull UUID uuid);

  /**
   *
   *
   * <pre>
   * Find all Users
   * </pre>
   *
   * @return List of Users
   * @see User
   * @since 1.0
   */
  @NotNull
  List<User> findAll();

  /**
   *
   *
   * <pre>
   * Find User by Username Containing a String
   * </pre>
   *
   * @param name String to search for in Username
   * @return Optional of User
   * @see User
   * @since 1.0
   */
  Iterable<User> findAllByUsernameContaining(String name);

  /**
   *
   *
   * <pre>
   * Find 20 User by Name containing a String
   * </pre>
   *
   * @param username String to search for in username
   * @return List of Users
   * @see User
   * @since 1.0
   */
  List<User> findTop20ByUsernameContainingIgnoreCaseOrderByUsernameDesc(String username);
}
