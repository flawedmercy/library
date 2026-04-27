package com.lab11.library.service;

import com.lab11.library.dto.ReviewDto;
import com.lab11.library.entity.*;
import com.lab11.library.exception.ResourceNotFoundException;
import com.lab11.library.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Book sampleBook;
    private Review sampleReview;

    @BeforeEach
    void setUp() {
        Author author = Author.builder().id(1L).name("Orwell").email("o@b.com").build();
        sampleBook = Book.builder().id(1L).title("1984").author(author).build();
        sampleReview = Review.builder()
                .id(1L)
                .reviewerName("Alice")
                .content("Great book, really enjoyed it!")
                .rating(5)
                .book(sampleBook)
                .build();
    }

    @Test
    void findByBook_returnsReviewsForBook() {
        when(reviewRepository.findByBookId(1L)).thenReturn(List.of(sampleReview));

        List<ReviewDto.Response> result = reviewService.findByBook(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getReviewerName()).isEqualTo("Alice");
    }

    @Test
    void findById_existingId_returnsReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));

        ReviewDto.Response result = reviewService.findById(1L);

        assertThat(result.getRating()).isEqualTo(5);
    }

    @Test
    void findById_missingId_throwsNotFoundException() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_validRequest_savesAndReturns() {
        ReviewDto.Request request = ReviewDto.Request.builder()
                .reviewerName("Alice")
                .content("Great book, really enjoyed it!")
                .rating(5)
                .bookId(1L)
                .build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(reviewRepository.save(any(Review.class))).thenReturn(sampleReview);

        ReviewDto.Response result = reviewService.create(request);

        assertThat(result.getReviewerName()).isEqualTo("Alice");
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void create_unknownBook_throwsNotFoundException() {
        ReviewDto.Request request = ReviewDto.Request.builder()
                .reviewerName("Alice")
                .content("Great book, really enjoyed it!")
                .rating(5)
                .bookId(99L)
                .build();
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.create(request))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void update_existingReview_updatesFields() {
        ReviewDto.Request request = ReviewDto.Request.builder()
                .reviewerName("Bob")
                .content("Actually it was just okay.")
                .rating(3)
                .bookId(1L)
                .build();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));
        when(reviewRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ReviewDto.Response result = reviewService.update(1L, request);

        assertThat(result.getReviewerName()).isEqualTo("Bob");
        assertThat(result.getRating()).isEqualTo(3);
    }

    @Test
    void delete_existingId_deletesReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));

        reviewService.delete(1L);

        verify(reviewRepository).deleteById(1L);
    }

    @Test
    void delete_missingId_throwsNotFoundException() {
        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
