package com.example.Notes.service;

import com.example.Notes.model.Tag;
import com.example.Notes.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class TagService {
    
    @Autowired
    private TagRepository tagRepository;
    
    // Initialize predefined tags on application startup
    @PostConstruct
    public void initializePredefinedTags() {
        createSystemTagIfNotExists(Tag.PASSWORD, "Sensitive information and passwords", "#F44336");
        createSystemTagIfNotExists(Tag.TODO, "Tasks and to-do items", "#2196F3");
        createSystemTagIfNotExists(Tag.SHORT_NOTE, "Quick notes and reminders", "#4CAF50");
        createSystemTagIfNotExists(Tag.IDEAS, "Creative ideas and brainstorming", "#FF9800");
        createSystemTagIfNotExists(Tag.PERSONAL, "Personal thoughts and diary entries", "#9C27B0");
        createSystemTagIfNotExists(Tag.EVENTS, "Events, meetings, and appointments", "#607D8B");
    }
    
    private void createSystemTagIfNotExists(String name, String description, String color) {
        if (tagRepository.findByNameIgnoreCase(name).isEmpty()) {
            Tag tag = new Tag(name, description, color, true);
            tagRepository.save(tag);
        }
    }
    
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
    
    public List<Tag> getSystemTags() {
        return tagRepository.findByIsSystemTagTrue();
    }
    
    public List<Tag> getCustomTags() {
        return tagRepository.findByIsSystemTagFalse();
    }
    
    public Optional<Tag> getTagById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return tagRepository.findById(id);
    }
    
    public Optional<Tag> getTagByName(String name) {
        return tagRepository.findByNameIgnoreCase(name);
    }
    
    public Tag createTag(Tag tag) {
        // Check if tag already exists
        Optional<Tag> existingTag = tagRepository.findByNameIgnoreCase(tag.getName());
        if (existingTag.isPresent()) {
            return existingTag.get();
        }
        
        // Set as custom tag
        tag.setSystemTag(false);
        return tagRepository.save(tag);
    }
    
    public Tag createOrGetTag(String tagName) {
        return tagRepository.findByNameIgnoreCase(tagName)
                .orElseGet(() -> {
                    Tag newTag = new Tag(tagName);
                    return tagRepository.save(newTag);
                });
    }
    
    public List<Tag> searchTags(String searchTerm) {
        return tagRepository.searchByName(searchTerm);
    }
    
    public List<Tag> getPopularTags() {
        return tagRepository.findPopularTags();
    }
    
    public void deleteTag(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent() && !tag.get().isSystemTag()) {
            tagRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete system tag or tag not found");
        }
    }
}
