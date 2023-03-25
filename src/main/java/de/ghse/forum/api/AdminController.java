package de.ghse.forum.api;

import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * AdminController is a REST controller for admin related endpoints.
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

  /**
   * REST endpoint for deleting a user.
   * @apiNote This endpoint is only accessible for admins under /api/v1/admin/user/{id}.
   * @param id the UUID of the user to be deleted
   * @see de.ghse.forum.model.User User
   */
  @DeleteMapping(path = "/user/{id}")
  public void deleteUser(@PathVariable("id") UUID id) {
    logger.info("Deleting user with id: " + id);
    userService.deleteUser(id);
  }

    /**
     * REST endpoint for deleting a post.
     * @apiNote This endpoint is only accessible for admins under /api/v1/admin/post/{id}.
     * @param id the UUID of the post to be deleted
     * @see de.ghse.forum.model.Post Post
     */
  @DeleteMapping(path = "/post/{id}")
  public void deletePost(@PathVariable("id") UUID id) {
    logger.info("Deleting post with id: " + id);
    postService.deletePost(id);
  }
}
