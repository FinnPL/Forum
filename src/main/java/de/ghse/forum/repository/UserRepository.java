package de.ghse.forum.repository;

import de.ghse.forum.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

     @NotNull Optional<User> findById(@NotNull UUID uuid);
     @NotNull List<User> findAll();

     Iterable<User> findAllByUsernameContaining(String name);
}
