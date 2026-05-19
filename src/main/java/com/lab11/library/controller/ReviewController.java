package com.lab11.library.controller;

import com.lab11.library.dto.ReviewDto;
import com.lab11.library.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDto.Response>> getByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.findByBook(bookId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReviewDto.Response> create(@Valid @RequestBody ReviewDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto.Response> update(@PathVariable Long id,
                                                     @Valid @RequestBody ReviewDto.Request request) {
        return ResponseEntity.ok(reviewService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}