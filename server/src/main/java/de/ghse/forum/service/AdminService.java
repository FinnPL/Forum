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

  public void recursivePostDelete(UUID id) {
    postRepository
        .findById(id)
        .ifPresent(
            post -> {
              commentRepository.deleteAll(commentRepository.findAllByPost(post));
              postRepository.delete(post);
            });
  }
}
