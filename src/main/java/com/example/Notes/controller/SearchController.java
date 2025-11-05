package com.example.Notes.controller;

import com.example.Notes.model.Note;
import com.example.Notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    
    @Autowired
    private NoteService noteService;
    
    @GetMapping
    public ResponseEntity<List<Note>> searchNotes(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String tag) {
        
        List<Note> results = noteService.searchWithFilters(q, categoryId, tag);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/by-tag") //tagName is the query parameter(after ?)
    public ResponseEntity<List<Note>> searchByTag(@RequestParam String tagName) {
        List<Note> results = noteService.getNotesByTag(tagName);
        return ResponseEntity.ok(results); //Request parm - Takes the qp and pass it as String to fn
    }
    
    @GetMapping("/by-tags")
    public ResponseEntity<List<Note>> searchByTags(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "false") boolean requireAll) {
        
        List<Note> results = noteService.getNotesByTags(tags, requireAll);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<Note>> getRecentNotes() {
        List<Note> results = noteService.getRecentNotes();
        return ResponseEntity.ok(results);
    }
}
