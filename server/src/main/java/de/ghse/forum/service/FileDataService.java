package de.ghse.forum.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for file data.
 *
 * @apiNote This service is mapped to the file system.
 */
@Service
public class FileDataService {

  @Value("${file.directory}")
  private String directory;

  /**
   * Searches for files with the given id.
   *
   * @param id of the file
   * @return the file with the given id
   * @throws IOException if the file could not be found
   */
  public File getFile(String id) throws IOException {
    if (createDirectory()) throw new IOException("Could not create directory");
    File[] files = getFilesWithID(id);
    if (files == null || files.length != 1) throw new IOException("File not found");
    return files[0];
  }

  /**
   * Uploads a file to the file system.
   *
   * @param file to be uploaded
   * @param id of the file
   * @throws IOException if the file could not be uploaded
   */
  public void saveFile(MultipartFile file, String id) throws IOException {
    if (createDirectory()) throw new IOException("Could not create directory");
    if (file.getContentType() == null | !file.getContentType().startsWith("image/"))
      throw new IOException("File type not supported");

    Pattern pattern = Pattern.compile("^[a-zA-Z]+/[-+.a-zA-Z0-9]+$");
    Matcher matcher = pattern.matcher(file.getContentType());
    if (!matcher.matches()) throw new IOException("File type not supported");
    String extension = matcher.group(0).split("/")[1].toLowerCase();
    deleteFile(id);
    file.transferTo(Paths.get(directory, id + "-" + UUID.randomUUID() + "." + extension));
  }

  /**
   * Deletes all files with the given id.
   *
   * @param id of the file
   * @throws IOException if the file could not be deleted
   */
  public void deleteFile(String id) throws IOException {
    if (createDirectory()) throw new IOException("Could not create directory");
    File[] files = getFilesWithID(id);
    if (files != null && files.length >= 1) {
      for (File f : files) {
        f.delete();
      }
    }
  }

  /**
   * Searches for all files with the given id.
   *
   * @param id of the file
   * @return a ResponseEntity with the status code and the file as a ByteArrayResource
   */
  private File[] getFilesWithID(String id) {
    return new File(directory).listFiles((dir, name) -> name.startsWith(id));
  }

  /**
   * Creates a directory if it does not exist.
   *
   * @return true if the directory could not be created
   */
  private boolean createDirectory() {
    boolean directoryExists = new File(directory).exists();
    if (!directoryExists) directoryExists = new File(directory).mkdir();
    return !directoryExists;
  }
}
