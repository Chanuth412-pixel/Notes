package com.example.Notes.controller;

import com.example.Notes.model.Note;
import com.example.Notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController //Handling HTTP requests and return responses
@RequestMapping("/api/notes") //Base URL for all endpoints in this controller
@CrossOrigin(origins = "http://localhost:3000")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping //No path variable
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @PatchMapping("/{id}/archived")
    public ResponseEntity<Note> archiveNoteStatus(@PathVariable Long id, @RequestParam boolean archived){
        return noteService.archiveNoteStatus(id, archived)
          .map (ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/{id}") // {id} is a path variable (Differntiate by that)
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Optional<Note> note = noteService.getNoteById(id);
        return note.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note createdNote = noteService.createOrUpdateNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note) {
        if (!noteService.getNoteById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        note.setId(id);
        Note updatedNote = noteService.createOrUpdateNote(note);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        if (!noteService.getNoteById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
