package de.ghse.forum.repository;

import de.ghse.forum.model.User;

import org.springframework.data.domain.Pageable;
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

  @NotNull
  Optional<User> findById(@NotNull UUID uuid);

  @Query("SELECT u FROM User u WHERE u.username LIKE %?1% ORDER BY u.username DESC")
  List<User> search(String username, Pageable pageable);
}
