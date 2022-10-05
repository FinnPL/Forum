package de.ghse.forum.api;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import de.ghse.forum.model.Post;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
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

  /**
   * Autowired Constructor
   *
   * @param postService Post Service
   * @param userService User Service
   * @see PostService
   * @see UserService
   * @since 1.0
   */
  @Autowired
  public PostController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  // debug Requests:
  // **************************************************************************************************************************************************

  /**
   *
   *
   * <pre>
   * Debug Request Only!
   * Get all Posts from Database
   * Location: <a href="http://localhost:8080/api/v1/post/all">/all</a>
   * </pre>
   *
   * @return List of all Posts in Database as PostResponse Objects in JSON Format
   * @see PostResponse
   * @see PostService#getAllPosts()
   * @since 1.0
   * @deprecated Debug Only!
   */
  @GetMapping(path = "/all")
  public List<PostResponse> getAllPosts() {
    return new PostResponse().convert(postService.getAllPosts());
  }

  // API Requests:
  // ****************************************************************************************************************************************************

  /**
   *
   *
   * <pre>
   * Api Post Request to add a Post to Database
   * Location: <a href="http://localhost:8080/api/v1/post/add">/add</a>
   * </pre>
   *
   * @param postRequest PostRequest Object in JSON Format
   * @return Response Entity with Status Code 201 and Location Header
   * @see PostRequest
   * @see PostService#addPost(Post)
   * @since 1.0
   */
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

  /**
   *
   *
   * <pre>
   * Api Get Request to get a Post by ID from Database
   * Location: <a href="http://localhost:8080/api/v1/post/{id}">/{id}</a>
   * </pre>
   *
   * @param id UUID of Post
   * @return PostResponse Object in JSON Format
   * @see PostResponse
   * @see PostService#getPostById(UUID)
   * @since 1.0
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
   *
   *
   * <pre>
   * Api Delete Request to delete a Post by ID from Database
   * Location: <a href="http://localhost:8080/api/v1/post/del/{id}">/del/{id}</a>
   * </pre>
   *
   * @param id UUID of Post
   * @return Response Entity with Status Code 200
   * @see PostService#deletePost(UUID)
   * @since 1.0
   */
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

  /**
   *
   *
   * <pre>
   * Api Put Request to Update a Post by ID in Database
   * Location: <a href="http://localhost:8080/api/v1/post/{id}">/{id}</a>
   * </pre>
   *
   * @param id UUID of Post
   * @param post Object in JSON Format
   * @return Response Entity with Status Code 200
   * @see PostRequest
   * @see PostService#updatePost(UUID, Post)
   * @since 1.0
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<PostResponse> updatePost(
      @PathVariable("id") UUID id, @Valid @NonNull @RequestBody Post post) {
    return ResponseEntity.ok()
        .body(
            new PostResponse()
                .convert(
                    postService
                        .updatePost(id, post)
                        .orElseThrow(
                            () ->
                                new ResponseStatusException(
                                    NOT_FOUND, "Can not update Post: \nPost not found"))));
  }

  /**
   *
   *
   * <pre>
   * Api Get Request for all Posts Containing a String
   * Location: <a href="http://localhost:8080/api/v1/post/search/{title}">/search/{title}</a>
   * </pre>
   *
   * @param title Title of Post as String
   * @return List of all Posts with Title in Database as PostResponse Objects in JSON Format
   * @see PostResponse
   * @see PostService#getAllByTitleContaining(String)
   * @since 1.0
   */
  @GetMapping(path = "search/{title}")
  public List<PostResponse> getAllByTitleContaining(@PathVariable("title") String title) {
    return new PostResponse().convert(postService.getAllByTitleContaining(title));
  }

  /**
   *
   *
   * <pre>
   * Api Get Request for all Posts of a User
   * Location: <a href="http://localhost:8080/api/v1/post/user/{id}">/user/{id}</a>
   * </pre>
   *
   * @param id UUID of User
   * @return List of all Posts of User in Database as PostResponse Objects in JSON Format
   * @see PostResponse
   * @see PostService#getAllByUser(de.ghse.forum.model.User)
   * @see UserService#findUserById(UUID)
   * @since 1.0
   */
  @RequestMapping(path = "/user/{id}/posts")
  public List<PostResponse> getAllByUser(@PathVariable("id") UUID id) {
    return new PostResponse()
        .convert(postService.getAllByUser(userService.findUserById(id).orElseThrow()));
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
    private UUID id;
    private String title;
    private String content;
    private UUID user_id;
    private String user_name;
    private Timestamp date;

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
        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setUser_id(post.getUser().getId());
        postResponse.setUser_name(post.getUser().getUsername());
        postResponse.setDate(post.getDate());
        postResponses.add(postResponse);
      }
      return postResponses;
    }

    /**
     * Converts a Post Object to a PostResponse Object
     * @param post Post Object
     * @return PostResponse Object
     * @see Post
     * @see PostResponse
     * @since 1.0
     */
    public PostResponse convert(Post post) {
      PostResponse postResponse = new PostResponse();
      postResponse.setId(post.getId());
      postResponse.setTitle(post.getTitle());
      postResponse.setContent(post.getContent());
      postResponse.setUser_id(post.getUser().getId());
      postResponse.setUser_name(post.getUser().getUsername());
      postResponse.setDate(post.getDate());
      return postResponse;
    }
  }
}
