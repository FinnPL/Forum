package de.ghse.forum.api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileDataController {

    public static String directory = "./files/";

    @PostMapping("/api/v1/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        file.transferTo(Paths.get(directory, file.getOriginalFilename()));

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/file").toUriString());
        return ResponseEntity.created(uri).body("File uploaded successfully");
    }

    @GetMapping("/api/v1/file")
    public ResponseEntity<Resource> loadFile(@RequestParam("file") String file) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.parseMediaType(Files.probeContentType(Path.of(directory + file))))); // (3) Content-Type: application/octet-stream
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(file).build().toString()); // (4) Content-Disposition: attachment; filename="demo-file.txt"

        return ResponseEntity.ok().headers(httpHeaders).body(new ByteArrayResource(Files.readAllBytes(Paths.get(directory, file))));
    }
}
