package com.example.Notes.service;

import com.example.Notes.model.Category;
import com.example.Notes.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return categoryRepository.findById(id);
    }

    public Category createOrUpdateCategory(Category category) {
        Objects.requireNonNull(category, "category must not be null");
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        categoryRepository.deleteById(id);
    }
}
