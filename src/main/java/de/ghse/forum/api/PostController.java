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

@RequestMapping("api/v1/post")
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;
  private final UserService userService;
  Logger logger = LoggerFactory.getLogger(PostController.class);

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
      e.printStackTrace();
      logger.error("Error deleting post " + e.getMessage());
    }
    return ResponseEntity.badRequest().build();
  }

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

  @GetMapping(path = "search/{query}/{page}")
  public List<PostResponse> search(
      @NotBlank @PathVariable("query") String query, @PathVariable("page") int page) {
    return new PostResponse().convert(postService.getSearchPage(query, page));
  }

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
