package com.lab11.library.service;

import com.lab11.library.dto.BookDto;
import com.lab11.library.entity.*;
import com.lab11.library.exception.*;
import com.lab11.library.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock BookRepository bookRepository;
    @Mock AuthorRepository authorRepository;
    @Mock CategoryRepository categoryRepository;

    @InjectMocks BookService bookService;

    private Author author;
    private Book book;

    @BeforeEach
    void setUp() {
        author = Author.builder().id(1L).name("Orwell").email("o@b.com").build();
        book   = Book.builder().id(1L).title("1984").isbn("111").author(author).build();
    }

    @Test
    void findAll_returnsMappedList() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        List<BookDto.Response> result = bookService.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("1984");
    }

    @Test
    void create_validRequest_savesBook() {
        BookDto.Request request = BookDto.Request.builder()
                .title("1984").isbn("111").authorId(1L).build();
        when(bookRepository.existsByIsbn("111")).thenReturn(false);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any())).thenReturn(book);

        BookDto.Response result = bookService.create(request);

        assertThat(result.getTitle()).isEqualTo("1984");
    }

    @Test
    void create_duplicateIsbn_throwsDuplicate() {
        BookDto.Request request = BookDto.Request.builder().title("1984").isbn("111").authorId(1L).build();
        when(bookRepository.existsByIsbn("111")).thenReturn(true);

        assertThatThrownBy(() -> bookService.create(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void create_unknownAuthor_throwsNotFound() {
        BookDto.Request request = BookDto.Request.builder().title("1984").authorId(99L).build();
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.create(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_existingId_callsDeleteById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.delete(1L);
        verify(bookRepository).deleteById(1L);
    }
}