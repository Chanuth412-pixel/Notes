package com.example.Notes.controller;

import com.example.Notes.model.Tag;
import com.example.Notes.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    
    @Autowired
    private TagService tagService;
    
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/system")
    public ResponseEntity<List<Tag>> getSystemTags() {
        List<Tag> tags = tagService.getSystemTags();
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/custom")
    public ResponseEntity<List<Tag>> getCustomTags() {
        List<Tag> tags = tagService.getCustomTags();
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/popular")
    public ResponseEntity<List<Tag>> getPopularTags() {
        List<Tag> tags = tagService.getPopularTags();
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Tag>> searchTags(@RequestParam String q) {
        List<Tag> tags = tagService.searchTags(q);
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        Optional<Tag> tag = tagService.getTagById(id);
        return tag.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag createdTag = tagService.createTag(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        try {
            tagService.deleteTag(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
