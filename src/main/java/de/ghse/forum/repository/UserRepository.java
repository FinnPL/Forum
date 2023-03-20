package de.ghse.forum.repository;

import de.ghse.forum.model.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  Optional<User> findByUsername(@NotNull String username);

  @NotNull
  Optional<User> findById(@NotNull UUID uuid);

  @Query("SELECT u FROM User u WHERE u.username LIKE %?1% ORDER BY u.username DESC")
  List<User> search(String username, Pageable pageable);

  @Query("UPDATE User u SET u.bio = :bio WHERE u.id = :id")
  @Modifying
  @Transactional
  void updateUser(@Param("bio") String bio, @Param("id") UUID id);
}
