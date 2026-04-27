package com.lab11.library.controller;

import com.lab11.library.dto.BookDto;
import com.lab11.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDto.Response>> getAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookDto.Response>> getByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.findByAuthor(authorId));
    }

    @PostMapping
    public ResponseEntity<BookDto.Response> create(@Valid @RequestBody BookDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto.Response> update(@PathVariable Long id,
                                                   @Valid @RequestBody BookDto.Request request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}