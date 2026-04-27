package com.lab11.library.service;

import com.lab11.library.dto.CategoryDto;
import com.lab11.library.entity.Category;
import com.lab11.library.exception.*;
import com.lab11.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto.Response> findAll() {
        return categoryRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CategoryDto.Response findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public CategoryDto.Response create(CategoryDto.Request request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category '" + request.getName() + "' already exists");
        }
        Category cat = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        Category saved = categoryRepository.save(cat);
        log.info("Created category id={} name={}", saved.getId(), saved.getName());
        return toResponse(saved);
    }

    @Transactional
    public CategoryDto.Response update(Long id, CategoryDto.Request request) {
        Category cat = getOrThrow(id);
        if (!cat.getName().equals(request.getName()) && categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category name already in use: " + request.getName());
        }
        cat.setName(request.getName());
        cat.setDescription(request.getDescription());
        log.info("Updated category id={}", id);
        return toResponse(categoryRepository.save(cat));
    }

    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        categoryRepository.deleteById(id);
        log.info("Deleted category id={}", id);
    }

    private Category getOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    private CategoryDto.Response toResponse(Category c) {
        return CategoryDto.Response.builder()
                .id(c.getId()).name(c.getName()).description(c.getDescription()).build();
    }
}