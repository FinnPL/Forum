package de.ghse.forum.api;

import de.ghse.forum.api.response.PostResponse;
import de.ghse.forum.api.response.UserResponse;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AdminController is a REST controller for admin related endpoints.
 *
 * @apiNote This controller is only accessible for admins under /api/v1/admin.
 * @see de.ghse.forum.model.Role User Roles
 * @see de.ghse.forum.api.AuthenticationController
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
  final Logger logger = LoggerFactory.getLogger(AdminController.class);
  private final UserService userService;
  private final PostService postService;

    @Value("${file.directory}")
    private String directory;

  /**
   * REST endpoint for deleting a user.
   *
   * @apiNote This endpoint is only accessible for admins under /api/v1/admin/user/{id}.
   * @param id the UUID of the user to be deleted
   * @see de.ghse.forum.model.User User
   */
  @DeleteMapping(path = "/user/{id}")
  public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") UUID id) {
    logger.info("Deleting user with id: " + id);
    return
        userService
            .findUserById(id)
            .map(
                user -> {
                  userService.deleteUser(id);
                  return ResponseEntity.ok(new UserResponse().convert(user));
                })
            .orElse(ResponseEntity.notFound().build());
  }

  /**
   * REST endpoint for deleting a post.
   *
   * @apiNote This endpoint is only accessible for admins under /api/v1/admin/post/{id}.
   * @param id the UUID of the post to be deleted
   * @see de.ghse.forum.model.Post Post
   */
  @DeleteMapping(path = "/post/{id}")
  public ResponseEntity<PostResponse> deletePost(@PathVariable("id") UUID id) {
    logger.info("Deleting post with id: " + id);
    return postService
            .getPostById(id)
        .map(
            post -> {
              postService.deletePost(id);
              return ResponseEntity.ok(new PostResponse().convert(post));
            })
        .orElse(ResponseEntity.notFound().build());
  }
    /**
     * Deletes a file with the given id.
     * @param id the id of the file to be deleted
     * @return a ResponseEntity with the status code
     * @throws IOException if the file could not be deleted
     */
    @DeleteMapping("file/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable String id) throws IOException {
        File[] files = new File(directory).listFiles((dir, name) -> name.startsWith(id));
        if (files != null) {
            for (File file : files) {
                Files.delete(Paths.get(file.getPath()));
            }
        }
        return ResponseEntity.ok("File deleted");
    }
}
