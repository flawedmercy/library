package com.lab11.library.service;

import com.lab11.library.dto.AuthorDto;
import com.lab11.library.entity.Author;
import com.lab11.library.exception.DuplicateResourceException;
import com.lab11.library.exception.ResourceNotFoundException;
import com.lab11.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorDto.Response> findAll() {
        return authorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AuthorDto.Response findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public AuthorDto.Response create(AuthorDto.Request request) {
        if (authorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Author with email " + request.getEmail() + " already exists");
        }
        Author author = Author.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        Author saved = authorRepository.save(author);
        log.info("Created author id={} name={}", saved.getId(), saved.getName());
        return toResponse(saved);
    }

    @Transactional
    public AuthorDto.Response update(Long id, AuthorDto.Request request) {
        Author author = getOrThrow(id);
        if (!author.getEmail().equals(request.getEmail())
                && authorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }
        author.setName(request.getName());
        author.setEmail(request.getEmail());
        log.info("Updated author id={}", id);
        return toResponse(authorRepository.save(author));
    }

    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        authorRepository.deleteById(id);
        log.info("Deleted author id={}", id);
    }

    private Author getOrThrow(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }

    private AuthorDto.Response toResponse(Author a) {
        return AuthorDto.Response.builder()
                .id(a.getId())
                .name(a.getName())
                .email(a.getEmail())
                .build();
    }
}