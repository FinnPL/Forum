package de.ghse.forum.api;

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
import java.util.List;

@RestController
public class FileDataController {

    public static String directory = "./files/";

    @PostMapping("/api/v1/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") List<MultipartFile> files) throws IOException {
        boolean directoryExists = new File(directory).exists();

        if (!directoryExists) directoryExists = new File(FileDataController.directory).mkdir();

        if (!directoryExists) return ResponseEntity.badRequest().body("Directory could not be created");

        for (MultipartFile file : files) {
            file.transferTo(Paths.get(directory, System.currentTimeMillis() + "_" + file.getOriginalFilename()));
        }
        return ResponseEntity.ok().body("File(s) uploaded successfully");

    }

    @GetMapping("/api/v1/file")
    public ResponseEntity<ByteArrayResource> loadFile(@RequestParam("file") String file) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.parseMediaType(Files.probeContentType(Path.of(directory + file))));
        httpHeaders.setContentDisposition(ContentDisposition.parse(ContentDisposition.attachment().filename(file).build().toString()));
        httpHeaders.setCacheControl("max-age=36000");

        return ResponseEntity.ok().headers(httpHeaders).body(new ByteArrayResource(Files.readAllBytes(Paths.get(directory, file))));
    }


    @DeleteMapping("/api/v1/file")
    public ResponseEntity<String> deleteFile(@RequestParam("file") String file) throws IOException {
        Files.delete(Paths.get(directory, file));
        return ResponseEntity.ok().body("File deleted successfully");
    }
}
