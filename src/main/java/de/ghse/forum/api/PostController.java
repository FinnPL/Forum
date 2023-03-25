package de.ghse.forum.api;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import de.ghse.forum.api.request.PostRequest;
import de.ghse.forum.api.response.PostResponse;
import de.ghse.forum.model.Post;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * PostController is a REST controller for post related endpoints.
 *
 * @apiNote This controller is accessible under /api/v1/post.
 * @see PostService PostService
 * @see de.ghse.forum.model.Post Post
 */
@RequestMapping("api/v1/post")
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;
  private final UserService userService;
  final Logger logger = LoggerFactory.getLogger(PostController.class);

  /**
   * REST endpoint for adding a new post.
   *
   * @apiNote This endpoint is accessible under /api/v1/post/add.
   * @param postRequest the JSON body of the request
   * @param principal the principal of the user (Spring internal)
   * @return the created post as PostResponse
   * @see PostRequest PostRequest
   */
  @PostMapping(path = "/add")
  public ResponseEntity<PostResponse> addPost(
      @RequestBody PostRequest postRequest, Principal principal) {
    logger.info("Adding post with title: " + postRequest.getTitle());
    try {
      Post post =
          Post.builder()
              .title(postRequest.getTitle())
              .content(postRequest.getContent())
              .user(userService.findbyUsername(principal.getName()).orElseThrow())
              .build();

      postService.addPost(post);
      URI uri =
          URI.create(
              ServletUriComponentsBuilder.fromCurrentContextPath()
                  .path("/api/v1/post/add")
                  .toUriString());
      return ResponseEntity.created(uri).body(new PostResponse().convert(post));
    } catch (Exception e) {
      logger.error("Error adding post" + e.getMessage());
    }
    return ResponseEntity.badRequest().build();
  }

  /**
   * REST endpoint for getting posts by id.
   *
   * @apiNote This endpoint is accessible under /api/v1/post/{id}.
   * @param id the UUID of the post to be retrieved
   * @return the post as PostResponse
   * @see PostResponse PostResponse
   * @see Post Post
   */
  @GetMapping(path = "/{id}")
  public ResponseEntity<PostResponse> getPostById(@PathVariable("id") UUID id) {
    return ResponseEntity.ok()
        .body(
            new PostResponse()
                .convert(
                    postService
                        .getPostById(id)
                        .orElseThrow(
                            () ->
                                new ResponseStatusException(
                                    NOT_FOUND, "Can not get Post: \nPost not found"))));
  }

  /**
   * REST endpoint for deleting posts by id.
   *
   * @param id the UUID of the post to be deleted
   * @param principal the principal of the user (Spring internal)
   * @return the deleted post as PostResponse
   * @apiNote This endpoint is accessible under /api/v1/post/del/{id}.
   * @see PostResponse PostResponse
   * @see Post Post
   */
  @DeleteMapping(path = "del/{id}")
  public ResponseEntity<PostResponse> deletePost(@PathVariable("id") UUID id, Principal principal) {
    logger.info("Deleting post with id: " + id);
    try {
      Post post = postService.getPostById(id).orElseThrow();
      if (post.getUser().getUsername().equals(principal.getName())) {
        postService.deletePost(id);
        return ResponseEntity.ok().body(new PostResponse().convert(post));
      } else {
        throw new ResponseStatusException(NOT_FOUND, "Can not delete Post: \nPost not found");
      }
    } catch (Exception e) {
      logger.error("Error deleting post " + e.getMessage());
    }
    return ResponseEntity.badRequest().build();
  }

  /**
   * REST endpoint for updating posts by id.
   *
   * @param id the UUID of the post to be updated
   * @param postRequest the JSON body of the request containing the new post data
   * @param principal the principal of the user (Spring internal)
   * @return the updated post as PostResponse
   * @apiNote This endpoint is accessible under /api/v1/post/{id}.
   * @see PostResponse PostResponse
   * @see Post Post
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<PostResponse> updatePost(
      @PathVariable("id") UUID id,
      @Valid @NonNull @RequestBody PostRequest postRequest,
      Principal principal) {
    logger.info("Updating post with id: " + id);
    try {
      Post post = postService.getPostById(id).orElseThrow();
      if (post.getUser().getUsername().equals(principal.getName())) {
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setEdited(true);
        postService.updatePost(id, post);
        return ResponseEntity.ok().body(new PostResponse().convert(post));
      } else {
        throw new ResponseStatusException(NOT_FOUND, "Can not update Post: \nPost not found");
      }
    } catch (Exception e) {
      logger.error("Error updating post" + e.getMessage());
    }
    return ResponseEntity.badRequest().build();
  }

  /**
   * REST endpoint for searching posts. Searches for posts containing the query in the title or
   * content.
   *
   * @param query the query to search for
   * @param page the page to be retrieved
   * @return the posts as PostResponse
   * @apiNote This endpoint is accessible under /api/v1/post/all.
   * @see PostResponse PostResponse
   * @see Post Post
   */
  @GetMapping(path = "search/{query}/{page}")
  public List<PostResponse> search(
      @NotBlank @PathVariable("query") String query, @PathVariable("page") int page) {
    return new PostResponse().convert(postService.getSearchPage(query, page));
  }

  /**
   * REST endpoint for getting Posts by user.
   *
   * @apiNote This endpoint is accessible under /api/v1/post/all.
   * @param id the UUID of the user
   * @param page the page to be retrieved
   * @return the posts as PostResponse
   * @see PostResponse PostResponse
   * @see Post Post
   */
  @GetMapping(path = "/user/{id}/{page}")
  public ResponseEntity<List<PostResponse>> getByUserByPage(
      @PathVariable("id") UUID id, @PathVariable("page") int page) {
    try {
      return ResponseEntity.ok()
          .body(
              new PostResponse()
                  .convert(
                      postService.getPostsByUserByPage(
                          userService.findUserById(id).orElseThrow(), page)));
    } catch (Exception e) {
      logger.error("Error getting posts by user" + e.getMessage());
    }
    return ResponseEntity.badRequest().build();
  }

  @GetMapping(path = "/page/{page}")
  public List<PostResponse> getAllByPage(@PathVariable("page") int page) {
    return new PostResponse().convert(postService.getNewestByPage(page));
  }
}
