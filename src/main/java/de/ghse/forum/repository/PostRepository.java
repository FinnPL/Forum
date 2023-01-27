package de.ghse.forum.repository;

import de.ghse.forum.model.Post;
import de.ghse.forum.model.User;


import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {


  @Query("SELECT p FROM Post p ORDER BY p.date DESC")
  List<Post> getNewestPostByPage( Pageable pageable);

  @Query("SELECT p FROM Post p WHERE p.user = ?1 ORDER BY p.date DESC")
  List<Post> findPostsByUserByPage(User user, Pageable pageable);

  //search for posts by title or content containing a string ignore-case and order by date descending
  @Query ("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%',?1,'%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%',?1,'%')) ORDER BY p.date DESC")
  List<Post> search(String query, Pageable pageable);


}
