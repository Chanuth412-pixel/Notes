package com.example.Notes.model;

import jakarta.persistence.*;

import java.security.PrivateKey;
import java.util.HashSet;
import java.util.Set;

@Entity    // A table in the database
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;  // The title of the note
    private String content; // The content of the note

    private boolean archived;

    @ManyToOne
    @JoinColumn(name = "User_id")
    private User user;
    
    // Many notes belong to one category
    @ManyToOne
    private Category category;

    // A note can have multiple tags and vice versa (Many to Many relationship)
    // Clean join table configuration with schema recreation
    @ManyToMany
    @JoinTable(
        name = "note_tags",
        joinColumns = @JoinColumn(name = "note_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Constructors
    public Note() {
        this.tags = new HashSet<>();
    }

    public Note(String title, String content) {
        this();
        this.title = title;
        this.content = content;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    
    //Getters and Setters for archived
    public boolean isArchived(){
        return archived;
    }

    public void setArchived(boolean status){
        this.archived = status;
    }

   
    
}
