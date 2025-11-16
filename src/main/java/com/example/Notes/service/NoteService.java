package com.example.Notes.service;

import com.example.Notes.model.Note;
import com.example.Notes.model.Tag;
import com.example.Notes.repository.NoteRepository;
import com.example.Notes.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TagRepository tagRepository;

    // Method to save or update a note
    @Transactional // To Ensure operations are atomic(If anything fails, rollback)
    public Note createOrUpdateNote(Note note) {
        Objects.requireNonNull(note, "note must not be null"); // NullpointerException

        // Resolve tags: convert transient tag objects to managed entities
        Set<Tag> managedTags = tagService.resolveTags(note.getTags());
        note.setTags(managedTags);
        return noteRepository.save(note);

        // Should Refactor this
        if (note.getTags() != null && !note.getTags().isEmpty()) {
            Set<Tag> managedTags = new HashSet<>();
            for (Tag tag : note.getTags()) {
                if (tag.getId() != null && tag.getId() > 0) {
                    // Tag has ID - fetch from database
                    Long tagId = tag.getId();
                    Optional<Tag> existingTag = tagRepository.findById(tagId);
                    existingTag.ifPresent(managedTags::add);
                } else if (tag.getName() != null && !tag.getName().trim().isEmpty()) {
                    // Tag has name but no ID - find or create
                    Tag managedTag = tagRepository.findByNameIgnoreCase(tag.getName().trim())
                            .orElseGet(() -> {
                                Tag newTag = new Tag(tag.getName().trim());
                                newTag.setSystemTag(false); // User-created tags are not system tags
                                if (tag.getDescription() != null) {
                                    newTag.setDescription(tag.getDescription());
                                }
                                if (tag.getColor() != null) {
                                    newTag.setColor(tag.getColor());
                                }
                                return tagRepository.save(newTag);
                            });
                    managedTags.add(managedTag);
                }
            }
            note.setTags(managedTags);
        }

        return noteRepository.save(note);
    }

    // Method to get all notes
    public List<Note> getAllNotes() {
        // Use fetch-join repository method so tags are loaded in the same transaction
        // and
        // Jackson can safely serialize Note -> tags without triggering lazy-loading
        return noteRepository.findAllWithTags();
    }

    // Method to get a note by its ID
    public Optional<Note> getNoteById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return noteRepository.findByIdWithTags(id);
    }

    // Method to delete a note by its ID
    public void deleteNote(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        noteRepository.deleteById(id);
    }

    // Search with optional filters: q, categoryId, tagName
    public List<Note> searchWithFilters(String q, Long categoryId, String tagName) {
        return noteRepository.findAll().stream()
                .filter(n -> {
                    boolean matches = true;
                    if (q != null && !q.isBlank()) {
                        String lower = q.toLowerCase(Locale.ROOT);
                        matches &= (n.getTitle() != null && n.getTitle().toLowerCase(Locale.ROOT).contains(lower))
                                || (n.getContent() != null && n.getContent().toLowerCase(Locale.ROOT).contains(lower));
                    }
                    if (categoryId != null) {
                        matches &= (n.getCategory() != null && Objects.equals(n.getCategory().getId(), categoryId));
                    }
                    if (tagName != null && !tagName.isBlank()) {
                        matches &= (n.getTags() != null
                                && n.getTags().stream().anyMatch(t -> tagName.equalsIgnoreCase(t.getName())));
                    }
                    return matches;
                })
                .collect(Collectors.toList());
    }

    // Get notes by a single tag name
    public List<Note> getNotesByTag(String tagName) {
        if (tagName == null || tagName.isBlank())
            return Collections.emptyList();
        return noteRepository.findAll().stream()
                .filter(n -> n.getTags() != null
                        && n.getTags().stream().anyMatch(t -> tagName.equalsIgnoreCase(t.getName())))
                .collect(Collectors.toList());
    }

    // Get notes by multiple tag names; if requireAll is true, note must have all
    // tags
    public List<Note> getNotesByTags(List<String> tagNames, boolean requireAll) {
        if (tagNames == null || tagNames.isEmpty())
            return Collections.emptyList();
        Set<String> lowerTags = tagNames.stream().map(s -> s.toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
        return noteRepository.findAll().stream()
                .filter(n -> {
                    if (n.getTags() == null)
                        return false;
                    Set<String> noteTagNames = n.getTags().stream()
                            .map(Tag::getName)
                            .filter(Objects::nonNull)
                            .map(s -> s.toLowerCase(Locale.ROOT))
                            .collect(Collectors.toSet());
                    if (requireAll) {
                        return noteTagNames.containsAll(lowerTags);
                    } else {
                        for (String t : lowerTags)
                            if (noteTagNames.contains(t))
                                return true;
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // Return recent notes (simple implementation: sort by id desc and return up to
    // 10)
    public List<Note> getRecentNotes() {
        return noteRepository.findAll().stream()
                .sorted(Comparator.comparing(Note::getId, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // Set the archived status of a note
    @Transactional
    public Optional<Note> archiveNoteStatus(Long id, boolean archived) {
        return noteRepository.findById(id)
                .map(note -> {
                    note.setArchived(archived);
                    return note;
                });
    }

}
