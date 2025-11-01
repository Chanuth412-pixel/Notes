package com.example.Notes.service;

import com.example.Notes.model.Note;
import com.example.Notes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    // Method to save a new note
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    // Method to get all notes
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Method to get a note by its ID
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);  // Return null if not found
    }

    // Method to delete a note by its ID
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
