package com.lab11.library.service;

import com.lab11.library.dto.AuthorDto;
import com.lab11.library.entity.Author;
import com.lab11.library.exception.DuplicateResourceException;
import com.lab11.library.exception.ResourceNotFoundException;
import com.lab11.library.repository.AuthorRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author sampleAuthor;

    @BeforeEach
    void setUp() {
        sampleAuthor = Author.builder()
                .id(1L)
                .name("George Orwell")
                .email("orwell@books.com")
                .build();
    }

    @Test
    void findAll_returnsAllAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(sampleAuthor));

        List<AuthorDto.Response> result = authorService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("George Orwell");
    }

    @Test
    void findById_existingId_returnsAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        AuthorDto.Response result = authorService.findById(1L);

        assertThat(result.getEmail()).isEqualTo("orwell@books.com");
    }

    @Test
    void findById_missingId_throwsNotFoundException() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_newEmail_savesAndReturns() {
        AuthorDto.Request request = new AuthorDto.Request("George Orwell", "orwell@books.com");
        when(authorRepository.existsByEmail("orwell@books.com")).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(sampleAuthor);

        AuthorDto.Response result = authorService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void create_duplicateEmail_throwsDuplicateException() {
        AuthorDto.Request request = new AuthorDto.Request("George Orwell", "orwell@books.com");
        when(authorRepository.existsByEmail("orwell@books.com")).thenReturn(true);

        assertThatThrownBy(() -> authorService.create(request))
                .isInstanceOf(DuplicateResourceException.class);

        verify(authorRepository, never()).save(any());
    }

    @Test
    void delete_existingId_deletesAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        authorService.delete(1L);

        verify(authorRepository).deleteById(1L);
    }

    @Test
    void delete_missingId_throwsNotFoundException() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_existingAuthor_updatesFields() {
        AuthorDto.Request request = new AuthorDto.Request("Eric Blair", "eric@books.com");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));
        when(authorRepository.existsByEmail("eric@books.com")).thenReturn(false);
        when(authorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AuthorDto.Response result = authorService.update(1L, request);

        assertThat(result.getName()).isEqualTo("Eric Blair");
    }
}