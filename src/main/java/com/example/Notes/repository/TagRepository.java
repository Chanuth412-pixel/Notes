package com.example.Notes.repository;

import com.example.Notes.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    // Find tag by name (case-insensitive)
    @Query("SELECT t FROM Tag t WHERE LOWER(t.name) = LOWER(:name)")
    Optional<Tag> findByNameIgnoreCase(@Param("name") String name);
    
    // Get all system tags
    List<Tag> findByIsSystemTagTrue();
    
    // Get all custom tags
    List<Tag> findByIsSystemTagFalse();
    
    // Search tags by name
    @Query("SELECT t FROM Tag t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Tag> searchByName(@Param("searchTerm") String searchTerm);
    
    // Get popular tags (tags with most notes)
    @Query("SELECT t FROM Tag t LEFT JOIN t.notes n " +
           "GROUP BY t ORDER BY COUNT(n) DESC")
    List<Tag> findPopularTags();
    
    // Get tags with note count
    @Query("SELECT t, COUNT(n) as noteCount FROM Tag t LEFT JOIN t.notes n GROUP BY t")
    List<Object[]> findTagsWithNoteCount();
}
