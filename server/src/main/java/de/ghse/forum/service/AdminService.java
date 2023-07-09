package de.ghse.forum.service;

import de.ghse.forum.repository.CommentRepository;
import de.ghse.forum.repository.PostRepository;
import de.ghse.forum.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  public final CommentRepository commentRepository;

  /**
   * Deletes a user and all of its posts and comments.
   *
   * @param id the UUID of the user to be deleted
   */
  public void recursiveUserDelete(UUID id) {
    userRepository
        .findById(id)
        .ifPresent(
            user -> {
              postRepository
                  .findAllByUser(user)
                  .forEach(
                      post -> {
                        commentRepository.deleteAll(commentRepository.findAllByPost(post));
                        postRepository.delete(post);
                      });
              commentRepository.deleteAll(commentRepository.findAllByUser(user));
              userRepository.delete(user);
            });
  }

  /**
   * Deletes a post and all of its comments.
   *
   * @param id the UUID of the post to be deleted
   */
  public void recursivePostDelete(UUID id) {
    postRepository
        .findById(id)
        .ifPresent(
            post -> {
              commentRepository.deleteAll(commentRepository.findAllByPost(post));
              postRepository.delete(post);
            });
  }

    /**
     * Deletes a comment.
     * @param id the UUID of the comment to be deleted
     */
    public void deleteComment(UUID id) {
        commentRepository.deleteById(id);
    }
}
