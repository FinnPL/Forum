package de.ghse.forum.repository;

import de.ghse.forum.model.Post;
import de.ghse.forum.model.User;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 *
 * <pre>
 * Post Repository Interface extends JpaRepository
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

  /**
   *
   *
   * <pre>
   * Find all Posts by Title Containing a String
   * </pre>
   *
   * @param title String to search for in Title
   * @return List of Posts
   * @see User
   * @see Post
   * @since 1.0
   */
  List<Post> findAllByTitleContaining(String title);

  /**
   *
   *
   * <pre>
   * Find all Posts by User
   * </pre>
   *
   * @param user User to search for
   * @return List of Posts
   * @see User
   * @see Post
   * @since 1.0
   */
  List<Post> findAllByUser(User user);

  /**
   *
   *
   * <pre>
   * Find 20 posts by Title or Content containing a String
   * </pre>
   *
   * @param title String to search for in Title
   * @param content String to search for in Content
   * @return List of Posts (max 20)
   * @see Post
   * @since 1.0
   */
  List<Post> findTop20ByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrderByDateDesc(
      String title, String content);

  /**
   *
   *
   * <pre>
   * Find newest 20 Posts from Database
   * </pre>
   *
   * @return List of Posts (max 20)
   * @see Post
   * @since 1.0
   */
  List<Post> findTop20ByOrderByDateDesc();

  /**
   *
   *
   * <pre>
   * Find newest 20 Posts after a given time from Database
   * </pre>
   *
   * @param date Date to search after
   * @return List of Posts (max 20)
   * @see Post
   * @since 1.0
   */
  List<Post> findTop20ByDateAfterOrderByDateDesc(Timestamp date);
}
