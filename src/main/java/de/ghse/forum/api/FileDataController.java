package de.ghse.forum.api;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileDataController {

  private final UserService userService;
  private final PostService postService;

  Logger logger = LoggerFactory.getLogger(CommentController.class);

  @Value("${file.directory}")
  private String directory;

  @PostMapping("/api/v1/file/profile")
  public ResponseEntity<String> uploadProfilePicture(
      @RequestParam("file") MultipartFile file, Principal principal) throws IOException {

    if (file.getContentType() != null && !file.getContentType().startsWith("image/"))
      return ResponseEntity.badRequest().body("File type not supported");

    if (createDirectory()) return ResponseEntity.badRequest().body("Could not create directory");
    String id = userService.findbyUsername(principal.getName()).orElseThrow().getId().toString();
    return getStringResponseEntity(id, file);
  }

  @GetMapping("/api/v1/file/profile/{id}")
  public ResponseEntity<ByteArrayResource> loadProfilePicture(@PathVariable("id") String id)
      throws IOException {
    return getByteArrayResourceResponseEntity(id);
  }

  @PostMapping("/api/v1/file/post/{id}")
  public ResponseEntity<String> uploadFile(
      @PathVariable("id") String id, @RequestParam("file") MultipartFile file, Principal principal)
      throws IOException {
    logger.info("Uploading files to Post with id: " + id);
    if (createDirectory()) return ResponseEntity.badRequest().body("Could not create directory");
    if (postService
        .getPostById(UUID.fromString(id))
        .orElseThrow()
        .getUser()
        .getId()
        .toString()
        .equals(userService.findbyUsername(principal.getName()).orElseThrow().getId().toString())) {
      if (file.getContentType() != null && !file.getContentType().startsWith("image/")) {
        return ResponseEntity.badRequest().body("File type not supported");
      }
      return getStringResponseEntity(id, file);
    }
    return ResponseEntity.badRequest().body("You are not allowed to upload files to this post");
  }

  @NotNull
  private ResponseEntity<String> getStringResponseEntity(
      @PathVariable("id") String id, @RequestParam("file") MultipartFile file) throws IOException {
    File[] files = new File(directory).listFiles((dir, name) -> name.startsWith(id));
    if (files != null && files.length >= 1) {
      for (File f : files) {
        if (!f.delete()) logger.error("Could not delete file: " + f.getName());
      }
    }
    file.transferTo(
        Paths.get(
            directory,
            id
                + "-"
                + UUID.randomUUID()
                + "."
                + file.getContentType().split("/")[1].toLowerCase()));
    return ResponseEntity.ok().body("File(s) uploaded successfully");
  }

  @GetMapping("/api/v1/file/post/{id}")
  public ResponseEntity<ByteArrayResource> loadFile(@PathVariable String id) throws IOException {
    return getByteArrayResourceResponseEntity(id);
  }

  @NotNull
  private ResponseEntity<ByteArrayResource> getByteArrayResourceResponseEntity(
      @PathVariable String id) throws IOException {
    HttpHeaders httpHeaders = new HttpHeaders();
    File[] files = new File(directory).listFiles((dir, name) -> name.startsWith(id));
    if (files == null || files.length != 1) return ResponseEntity.notFound().build();
    httpHeaders.setContentType(
        MediaType.parseMediaType(Files.probeContentType(Path.of(files[0].getAbsolutePath()))));
    httpHeaders.setContentDisposition(
        ContentDisposition.parse(
            ContentDisposition.attachment().filename(files[0].getName()).build().toString()));
    httpHeaders.setCacheControl("max-age=36000");
    return ResponseEntity.ok()
        .headers(httpHeaders)
        .body(new ByteArrayResource(Files.readAllBytes(Paths.get(files[0].getAbsolutePath()))));
  }

  @DeleteMapping("/api/v1/file")
  public ResponseEntity<String> deleteFile(@RequestParam("file") String file) throws IOException {
    Files.delete(Paths.get(directory, file));
    return ResponseEntity.ok().body("File deleted successfully");
  }

  public boolean createDirectory() {
    boolean directoryExists = new File(directory).exists();

    if (!directoryExists) directoryExists = new File(directory).mkdir();

    return !directoryExists;
  }
}