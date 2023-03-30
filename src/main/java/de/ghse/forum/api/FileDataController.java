package de.ghse.forum.api;

import de.ghse.forum.service.FileDataService;
import de.ghse.forum.service.PostService;
import de.ghse.forum.service.UserService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileDataController is a REST controller for file related endpoints.
 *
 * @apiNote This controller is accessible under /api/v1/file.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/file")
public class FileDataController {

  private final UserService userService;
  private final PostService postService;
  private final FileDataService fileDataService;

  Logger logger = LoggerFactory.getLogger(CommentController.class);



  /**
   * REST endpoint for uploading a profile picture.
   *
   * @apiNote This endpoint is accessible under /api/v1/file/profile.
   * @param file to be uploaded
   * @param principal the user who is uploading the file (Spring internal)
   * @return a ResponseEntity with the status code
   */
  @PostMapping("/profile")
  public ResponseEntity<String> uploadProfilePicture(
      @RequestParam("file") MultipartFile file, Principal principal) {

    String id = userService.findbyUsername(principal.getName()).orElseThrow().getId().toString();
    logger.info("Uploading profile picture for user with id: " + id);
    try {
      fileDataService.saveFile(file, id);
    } catch (IOException e) {
      logger.error("Could not upload profile picture for user with id: " + id);
      return ResponseEntity.badRequest().body("Could not upload file");
    }
    return ResponseEntity.ok("File uploaded");
  }

  /**
   * REST endpoint for downloading a profile picture.
   *
   * @apiNote This endpoint is accessible under /api/v1/file/profile/{id}.
   * @param id of the user
   * @return a ResponseEntity with the status code
   */
  @GetMapping("/profile/{id}")
  public ResponseEntity<ByteArrayResource> loadProfilePicture(@PathVariable("id") String id) {
    logger.info("Downloading profile picture for user with id: " + id);
    try {
      return buildResponseEntity(fileDataService.getFile(id));
    } catch (IOException e) {
      logger.error("Could not download profile picture for user with id: " + id);
      return ResponseEntity.badRequest().build();
    }
  }


  /**
   * REST endpoint for uploading a file allocated to a post.
   *
   * @apiNote This endpoint is accessible under /api/v1/file/post/{id}. The Post must be owned by the Requesting User.
   * @param id of the post
   * @return a ResponseEntity with the status code
   */
  @PostMapping("/post/{id}")
  public ResponseEntity<String> uploadFile(
      @PathVariable("id") String id, @RequestParam("file") MultipartFile file, Principal principal){
    logger.info("Uploading files to Post with id: " + id);
    try {
      if (isAllowedToUploadFile(id, principal)) {
        fileDataService.saveFile(file, id);
        return ResponseEntity.ok("File uploaded");
      }
      else {
        return ResponseEntity.badRequest().body("You are not allowed to upload files to this post");
      }
    }catch (IOException e) {
      logger.error("Could not upload file to post with id: " + id);
      return ResponseEntity.badRequest().body("Could not upload file");
    }
  }

  /**
   * REST endpoint for downloading a file allocated to a post.
   *
   * @apiNote This endpoint is accessible under /api/v1/file/post/{id}.
   * @param id of the post
   * @return a ResponseEntity with the status code and the file as a ByteArrayResource
   */
  @GetMapping("/post/{id}")
  public ResponseEntity<ByteArrayResource> loadFile(@PathVariable String id) {
    logger.info("Downloading file from Post with id: " + id);
    try {
      return buildResponseEntity(fileDataService.getFile(id));
    } catch (IOException e) {
      logger.error("Could not download file from post with id: " + id);
      return ResponseEntity.badRequest().build();
    }
  }

    /**
     * REST endpoint for deleting a file allocated to a post.
     *
     * @apiNote This endpoint is accessible under /api/v1/file/post/{id}. The Post must be owned by the Requesting User.
     * @param id of the post
     * @return a ResponseEntity with the status code
     */
  @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable String id, Principal principal) {
        logger.info("Deleting file from Post with id: " + id);
        try {
            if (isAllowedToUploadFile(id, principal)) {
            fileDataService.deleteFile(id);
            return ResponseEntity.ok("File deleted");
            }
            else {
            return ResponseEntity.badRequest().body("You are not allowed to delete files from this post");
            }
        }catch (IOException e) {
            logger.error("Could not delete file from post with id: " + id);
            return ResponseEntity.badRequest().body("Could not delete file");
        }
  }


    /**
     * Helper method for creating a ResponseEntity with a ByteArrayResource.
     *
     * @param file to be read
     * @return a ResponseEntity with the status code and the file as a ByteArrayResource
     * @throws IOException if the file could not be read
     */
  @NotNull
  private ResponseEntity<ByteArrayResource> buildResponseEntity(File file) throws IOException {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(
            MediaType.parseMediaType(Files.probeContentType(Path.of(file.getAbsolutePath()))));
    httpHeaders.setContentDisposition(
            ContentDisposition.parse(
                    ContentDisposition.attachment().filename(file.getName()).build().toString()));
    httpHeaders.setCacheControl("max-age=36000");
    return ResponseEntity.ok()
            .headers(httpHeaders)
            .body(new ByteArrayResource(Files.readAllBytes(Paths.get(file.getAbsolutePath()))));
  }

  /**
   * Helper method for checking if the user is allowed to upload a file to a post.
   *
   * @param id of the post
   * @param principal the user who is uploading the file (Spring internal)
   * @return true if the user is allowed to upload a file to the post, false otherwise
   */
  private boolean isAllowedToUploadFile(String id, Principal principal) {
    return postService
            .getPostById(UUID.fromString(id))
            .orElseThrow()
            .getUser()
            .getId()
            .toString()
            .equals(userService.findbyUsername(principal.getName()).orElseThrow().getId().toString());
  }
}
