package com.example.Notes.repository;

import com.example.Notes.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // You can add custom queries here if necessary
}
