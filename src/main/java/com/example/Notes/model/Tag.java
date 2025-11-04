package com.example.Notes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"notes"})
public class Tag {

    // Predefined system tag names used across the app
    public static final String PASSWORD = "password";
    public static final String TODO = "todo";
    public static final String SHORT_NOTE = "short_note";
    public static final String IDEAS = "ideas";
    public static final String PERSONAL = "personal";
    public static final String EVENTS = "events";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // The name of the tag

    private String description;
    private String color;

    private boolean isSystemTag = false;

    // Many notes can have many tags (Many to Many relationship)
    @ManyToMany(mappedBy = "tags")
    private Set<Note> notes = new HashSet<>();

    // Constructors
    public Tag() {
        this.notes = new HashSet<>();
    }

    public Tag(String name) {
        this();
        this.name = name;
    }

    public Tag(String name, String description, String color, boolean isSystemTag) {
        this();
        this.name = name;
        this.description = description;
        this.color = color;
        this.isSystemTag = isSystemTag;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isSystemTag() {
        return isSystemTag;
    }

    public void setSystemTag(boolean systemTag) {
        isSystemTag = systemTag;
    }
}
