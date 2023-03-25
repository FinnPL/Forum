package de.ghse.forum.service;

import de.ghse.forum.model.User;
import de.ghse.forum.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Service for users.
 *
 * @apiNote This service is mapped to the database table "user".
 * @see User User
 * @see de.ghse.forum.repository.UserRepository UserRepository
 */
@Service
@RequiredArgsConstructor
public class UserService {
  @Value("${page.size}")
  private int PAGE_SIZE;

  private final UserRepository UserRepository;

  /**
   * Find a user by its username.
   * @param username The username of the user.
   * @return The user.
   */
  public @NotNull Optional<User> findbyUsername(String username) {
    return UserRepository.findByUsername(username);
  }

    /**
     * Find a user by its id.
     * @param id The id of the user.
     * @return The user.
     */
  public @NotNull Optional<User> findUserById(UUID id) {
    return UserRepository.findById(id);
  }

  /**
   * Search for users by their username.
   * @param username The username to search for.
   * @param page The page to get.
   * @return A list of users.
   */
  public List<User> search(String username, int page) {
    return UserRepository.search(username, PageRequest.of(page, PAGE_SIZE));
  }

  /**
   * Delete a user from the database.
   * @param id The id of the user to delete.
   */
  public void deleteUser(UUID id) {
    UserRepository.deleteById(id);
  }

    /**
     * Update a user in the database.
     * @param bio The new bio of the user.
     * @param id The id of the user to update.
     */
  public void updateUser(String bio, UUID id) {
    UserRepository.updateUser(bio, id);
  }
}
