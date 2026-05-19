package com.lab11.library.service;

import com.lab11.library.dto.BookDto;
import com.lab11.library.entity.Author;
import com.lab11.library.entity.Book;
import com.lab11.library.entity.Category;
import com.lab11.library.exception.DuplicateResourceException;
import com.lab11.library.exception.ResourceNotFoundException;
import com.lab11.library.repository.AuthorRepository;
import com.lab11.library.repository.BookRepository;
import com.lab11.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BookDto.Response findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public List<BookDto.Response> findByAuthor(Long authorId) {
        return bookRepository.findByAuthorId(authorId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Page<BookDto.Response> searchBooks(String keyword,
                                              Long authorId,
                                              Long categoryId,
                                              Integer publishedYear,
                                              int page,
                                              int size,
                                              String sortBy,
                                              String direction) {

        List<Book> filtered = bookRepository.findAll()
                .stream()
                .filter(book -> keyword == null || keyword.isBlank()
                        || safeContains(book.getTitle(), keyword)
                        || safeContains(book.getIsbn(), keyword))
                .filter(book -> authorId == null
                        || (book.getAuthor() != null && book.getAuthor().getId().equals(authorId)))
                .filter(book -> categoryId == null
                        || (book.getCategory() != null && book.getCategory().getId().equals(categoryId)))
                .filter(book -> publishedYear == null
                        || (book.getPublishedYear() != null && book.getPublishedYear().equals(publishedYear)))
                .toList();

        Comparator<Book> comparator = switch (sortBy == null ? "title" : sortBy) {
            case "id" -> Comparator.comparing(Book::getId, Comparator.nullsLast(Long::compareTo));
            case "isbn" -> Comparator.comparing(Book::getIsbn, Comparator.nullsLast(String::compareToIgnoreCase));
            case "publishedYear" -> Comparator.comparing(Book::getPublishedYear, Comparator.nullsLast(Integer::compareTo));
            default -> Comparator.comparing(Book::getTitle, Comparator.nullsLast(String::compareToIgnoreCase));
        };

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        List<BookDto.Response> responses = filtered.stream()
                .sorted(comparator)
                .map(this::toResponse)
                .toList();

        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        int start = Math.min(safePage * safeSize, responses.size());
        int end = Math.min(start + safeSize, responses.size());

        Pageable pageable = PageRequest.of(safePage, safeSize);

        log.info("Searching books keyword={} authorId={} categoryId={} year={} page={} size={} sortBy={} direction={}",
                keyword, authorId, categoryId, publishedYear, safePage, safeSize, sortBy, direction);

        return new PageImpl<>(responses.subList(start, end), pageable, responses.size());
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

    private boolean safeContains(String source, String keyword) {
        return source != null && keyword != null
                && source.toLowerCase().contains(keyword.toLowerCase());
    }

    private BookDto.Response toResponse(Book book) {
        return BookDto.Response.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publishedYear(book.getPublishedYear())
                .authorName(book.getAuthor() != null ? book.getAuthor().getName() : null)
                .categoryName(book.getCategory() != null ? book.getCategory().getName() : null)
                .build();
    }
}