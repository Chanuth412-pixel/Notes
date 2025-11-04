package com.example.Notes.repository;

import com.example.Notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

	// Fetch notes with tags to avoid lazy-loading during serialization
	@Query("select distinct n from Note n left join fetch n.tags")
	List<Note> findAllWithTags();

	@Query("select n from Note n left join fetch n.tags where n.id = :id")
	Optional<Note> findByIdWithTags(@Param("id") Long id);
}
