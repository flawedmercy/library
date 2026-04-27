package com.lab11.library.service;

import com.lab11.library.dto.ReviewDto;
import com.lab11.library.entity.*;
import com.lab11.library.exception.ResourceNotFoundException;
import com.lab11.library.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public List<ReviewDto.Response> findByBook(Long bookId) {
        return reviewRepository.findByBookId(bookId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    public ReviewDto.Response findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public ReviewDto.Response create(ReviewDto.Request request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", request.getBookId()));

        Review review = Review.builder()
                .reviewerName(request.getReviewerName())
                .content(request.getContent())
                .rating(request.getRating())
                .book(book)
                .build();

        Review saved = reviewRepository.save(review);
        log.info("Created review id={} for book id={}", saved.getId(), book.getId());
        return toResponse(saved);
    }

    @Transactional
    public ReviewDto.Response update(Long id, ReviewDto.Request request) {
        Review review = getOrThrow(id);
        review.setReviewerName(request.getReviewerName());
        review.setContent(request.getContent());
        review.setRating(request.getRating());
        log.info("Updated review id={}", id);
        return toResponse(reviewRepository.save(review));
    }

    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        reviewRepository.deleteById(id);
        log.info("Deleted review id={}", id);
    }

    private Review getOrThrow(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", id));
    }

    private ReviewDto.Response toResponse(Review r) {
        return ReviewDto.Response.builder()
                .id(r.getId())
                .reviewerName(r.getReviewerName())
                .content(r.getContent())
                .rating(r.getRating())
                .bookId(r.getBook().getId())
                .bookTitle(r.getBook().getTitle())
                .build();
    }
}