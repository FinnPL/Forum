package de.ghse.forum.api;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import de.ghse.forum.model.Post;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 *
 * <pre>
 * Post Controller
 * Location's under: <a href="http://localhost:8080/api/v1/post/">/post</a>
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
@RequestMapping("api/v1/post")
@RestController
public class PostController {

  private final PostService postService;
  private final UserService userService;

  @Autowired
  public PostController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  @PostMapping(path = "/add")
  public ResponseEntity<PostResponse> addPost(@RequestBody PostRequest postRequest) {
    Post post = new Post();
    post.setUser(userService.findUserById(UUID.fromString(postRequest.getUser_id())).orElseThrow());
    post.setTitle(postRequest.getTitle());
    post.setContent(postRequest.getContent());
    postService.addPost(post);
    URI uri =
        URI.create(
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/post/add")
                .toUriString());
    return ResponseEntity.created(uri).body(new PostResponse().convert(post));
  }

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

  @DeleteMapping(path = "del/{id}")
  public ResponseEntity<PostResponse> deletePost(@PathVariable("id") UUID id) {
    return ResponseEntity.ok()
        .body(
            new PostResponse()
                .convert(
                    postService
                        .deletePost(id)
                        .orElseThrow(
                            () ->
                                new ResponseStatusException(
                                    NOT_FOUND, "Can not delete Post: \nPost not found"))));
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<PostResponse> updatePost(
      @PathVariable("id") UUID id, @Valid @NonNull @RequestBody PostRequest postRequest) {
    Post post = new Post();
    post.setUser(userService.findUserById(UUID.fromString(postRequest.getUser_id())).orElseThrow());
    post.setTitle(postRequest.getTitle());
    post.setContent(postRequest.getContent());
    return ResponseEntity.ok()
        .body(
            new PostResponse()
                .convert(
                    postService
                        .updatePost(id,post)
                        .orElseThrow(
                            () ->
                                new ResponseStatusException(
                                    NOT_FOUND, "Can not update Post: \nPost not found"))));
  }

  @GetMapping(path = "search/{query}/{page}")
  public List<PostResponse> search(
      @NotBlank @PathVariable("query") String query, @PathVariable("page") int page) {
    return new PostResponse().convert(postService.getSearchPage(query, page));
  }

  @GetMapping(path = "/user/{id}/{page}")
  public List<PostResponse> getByUserByPage(
      @PathVariable("id") UUID id, @PathVariable("page") int page) {
    return new PostResponse()
        .convert(
            postService.getPostsByUserByPage(userService.findUserById(id).orElseThrow(), page));
  }

  @GetMapping(path = "/page/{page}")
  public List<PostResponse> getAllByPage(@PathVariable("page") int page) {
    return new PostResponse().convert(postService.getNewestByPage(page));
  }

  // Response and Request Classes:
  // ********************************************************************************************************************************************

  /**
   *
   *
   * <pre>
   * PostRequest Class for API Requests
   * </pre>
   *
   * @since 1.0
   * @see Post
   * @see PostController
   */
  @Data
  public static class PostRequest {
    private String title;
    private String content;
    private String user_id;
  }

  /**
   *
   *
   * <pre>
   * PostResponse Class for API Responses
   * </pre>
   *
   * @since 1.0
   * @see Post
   * @see PostController
   */
  @Data
  public static class PostResponse {
    private String id;
    private String title;
    private String content;
    private String user_id;
    private String user_name;
    private String date;

    /**
     *
     *
     * <pre>
     * Converts a List of Posts to a List of PostResponses
     * </pre>
     *
     * @param posts List of Post Objects
     * @return PostResponse List of PostResponse Objects
     * @see Post
     * @see PostResponse
     * @since 1.0
     */
    public List<PostResponse> convert(List<Post> posts) {
      List<PostResponse> postResponses = new ArrayList<>();
      for (Post post : posts) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(String.valueOf(post.getId()));
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setUser_id(String.valueOf(post.getUser().getId()));
        postResponse.setUser_name(post.getUser().getUsername());
        postResponse.setDate(String.valueOf(post.getDate()));
        postResponses.add(postResponse);
      }
      return postResponses;
    }

    /**
     * Converts a Post Object to a PostResponse Object
     *
     * @param post Post Object
     * @return PostResponse Object
     * @see Post
     * @see PostResponse
     * @since 1.0
     */
    public PostResponse convert(Post post) {
      PostResponse postResponse = new PostResponse();
      postResponse.setId(String.valueOf(post.getId()));
      postResponse.setTitle(post.getTitle());
      postResponse.setContent(post.getContent());
      postResponse.setUser_id(String.valueOf(post.getUser().getId()));
      postResponse.setUser_name(post.getUser().getUsername());
      postResponse.setDate(String.valueOf(post.getDate()));
      return postResponse;
    }
  }
}
