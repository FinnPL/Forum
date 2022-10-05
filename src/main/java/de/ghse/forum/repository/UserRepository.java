package de.ghse.forum.repository;

import de.ghse.forum.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <pre>
 *User Repository Interface extends JpaRepository
 * </pre>
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

     /**
      * <pre>
      *Find User by ID
      * </pre>
      * @param uuid ID of User
      * @return Optional of User
      * @see User
      * @since 1.0
      */
     @NotNull Optional<User> findById(@NotNull UUID uuid);

     /**
      * <pre>
      *Find all Users
      * </pre>
      * @return List of Users
      * @see User
      * @since 1.0
      */
     @NotNull List<User> findAll();

        /**
        * <pre>
        *Find User by Username Containing a String
        * </pre>
        * @param name String to search for in Username
        * @return Optional of User
        * @see User
        * @since 1.0
        */
     Iterable<User> findAllByUsernameContaining(String name);
}
