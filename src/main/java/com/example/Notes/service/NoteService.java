package com.example.Notes.service;

import com.example.Notes.model.Note;
import com.example.Notes.model.Tag;
import com.example.Notes.repository.NoteRepository;
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
    private TagService tagService;

    // Method to save or update a note
    @Transactional
    public Note createOrUpdateNote(Note note) {
        Objects.requireNonNull(note, "note must not be null"); // Ensure the note is not null

        // Resolve tags: Convert transient tag objects to managed entities (either
        // existing or new)
        List<Tag> managedTags = tagService.resolveTags(new ArrayList<>(note.getTags())); // Convert Set to List for
                                                                                         // resolveTags

        // Set the resolved tags (List<Tag>) to the note
        note.setTags(new HashSet<>(managedTags)); // Optionally convert back to Set if needed

        // Save or update the note (depending on whether it's new or an update)
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
