package com.lab11.library.service;

import com.lab11.library.dto.BookDto;
import com.lab11.library.entity.*;
import com.lab11.library.exception.*;
import com.lab11.library.repository.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public List<BookDto.Response> findAll() {
        return bookRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public BookDto.Response findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public List<BookDto.Response> findByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public Page<BookDto.Response> searchBooks(String keyword,
                                              Long authorId,
                                              Long categoryId,
                                              Integer publishedYear,
                                              int page,
                                              int size,
                                              String sortBy,
                                              String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Book> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), like),
                        cb.like(cb.lower(root.get("isbn")), like)
                ));
            }

            if (authorId != null) {
                predicates.add(cb.equal(root.get("author").get("id"), authorId));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (publishedYear != null) {
                predicates.add(cb.equal(root.get("publishedYear"), publishedYear));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        log.info("Searching books keyword={} authorId={} categoryId={} year={} page={} size={} sortBy={} direction={}",
                keyword, authorId, categoryId, publishedYear, page, size, sortBy, direction);

        return bookRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Transactional
    public BookDto.Response create(BookDto.Request request) {
        if (request.getIsbn() != null && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book with ISBN " + request.getIsbn() + " already exists");
        }
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", request.getAuthorId()));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .publishedYear(request.getPublishedYear())
                .author(author)
                .category(category)
                .build();

        Book saved = bookRepository.save(book);
        log.info("Created book id={} title={}", saved.getId(), saved.getTitle());
        return toResponse(saved);
    }

    @Transactional
    public BookDto.Response update(Long id, BookDto.Request request) {
        Book book = getOrThrow(id);

        if (request.getIsbn() != null
                && !request.getIsbn().equals(book.getIsbn())
                && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("ISBN already in use: " + request.getIsbn());
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", request.getAuthorId()));
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
        }

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setAuthor(author);
        book.setCategory(category);

        log.info("Updated book id={}", id);
        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        bookRepository.deleteById(id);
        log.info("Deleted book id={}", id);
    }

    private Book getOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    private BookDto.Response toResponse(Book b) {
        return BookDto.Response.builder()
                .id(b.getId())
                .title(b.getTitle())
                .isbn(b.getIsbn())
                .publishedYear(b.getPublishedYear())
                .authorName(b.getAuthor() != null ? b.getAuthor().getName() : null)
                .categoryName(b.getCategory() != null ? b.getCategory().getName() : null)
                .build();
    }
}
