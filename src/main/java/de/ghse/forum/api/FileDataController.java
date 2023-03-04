package de.ghse.forum.api;

import de.ghse.forum.service.UserService;
import lombok.RequiredArgsConstructor;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileDataController {

    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(CommentController.class);


    @Value("${file.directory}")
    private String directory;

    @PostMapping("/api/v1/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") List<MultipartFile> files) throws IOException {
        logger.info("File upload request received");

        if (createDirectory()) return ResponseEntity.badRequest().body("Could not create directory");

        for (MultipartFile file : files) {
            file.transferTo(Paths.get(directory, System.currentTimeMillis() + "_" + file.getOriginalFilename()));
        }
        return ResponseEntity.ok().body("File(s) uploaded successfully");

    }

    @PostMapping("/api/v1/file/profile")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {

        if (file.getContentType() != null && !file.getContentType().startsWith("image/"))
            return ResponseEntity.badRequest().body("File type not supported");

        if (createDirectory()) return ResponseEntity.badRequest().body("Could not create directory");

        file.transferTo(Paths.get(directory, userService.findbyUsername(principal.getName()).orElseThrow().getId().toString() + "." + file.getContentType().split("/")[1].toLowerCase()));

        return ResponseEntity.ok().body("File(s) uploaded successfully");

    }

    public boolean createDirectory() {
        boolean directoryExists = new File(directory).exists();

        if (!directoryExists) directoryExists = new File(directory).mkdir();

        return !directoryExists;
    }

    @GetMapping("/api/v1/file/profile/{id}")
    public ResponseEntity<ByteArrayResource> loadProfilePicture(@PathVariable("id") String id) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        File[] files = new File(directory).listFiles((dir, name) -> name.startsWith(id));
        if (files == null || files.length != 1) return ResponseEntity.notFound().build();
        httpHeaders.setContentType(MediaType.parseMediaType(Files.probeContentType(Path.of(files[0].getAbsolutePath()))));
        httpHeaders.setContentDisposition(ContentDisposition.parse(ContentDisposition.attachment().filename(files[0].getName()).build().toString()));
        httpHeaders.setCacheControl("max-age=36000");
        httpHeaders.setAccessControlAllowOrigin("*");

        return ResponseEntity.ok().headers(httpHeaders).body(new ByteArrayResource(Files.readAllBytes(Paths.get(files[0].getAbsolutePath()))));
    }


    @GetMapping("/api/v1/file")
    public ResponseEntity<ByteArrayResource> loadFile(@RequestParam("file") String file) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(Files.probeContentType(Path.of(directory + file))));
        httpHeaders.setContentDisposition(ContentDisposition.parse(ContentDisposition.attachment().filename(file).build().toString()));
        httpHeaders.setCacheControl("max-age=36000");
        httpHeaders.setAccessControlAllowOrigin("*");

        return ResponseEntity.ok().headers(httpHeaders).body(new ByteArrayResource(Files.readAllBytes(Paths.get(directory, file))));
    }


    @DeleteMapping("/api/v1/file")
    public ResponseEntity<String> deleteFile(@RequestParam("file") String file) throws IOException {
        Files.delete(Paths.get(directory, file));
        return ResponseEntity.ok().body("File deleted successfully");
    }
}
