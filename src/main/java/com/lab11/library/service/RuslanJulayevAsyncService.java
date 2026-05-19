package com.lab11.library.service;

import com.lab11.library.dto.RuslanJulayevAsyncDto;
import com.lab11.library.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuslanJulayevAsyncService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ReviewRepository reviewRepository;
    private final RuslanJulayevFileRepository fileRepository;

    @Async
    public CompletableFuture<RuslanJulayevAsyncDto> countBooksAsync() {
        log.info("Async process started: count books");
        long count = bookRepository.count();
        return CompletableFuture.completedFuture(
                new RuslanJulayevAsyncDto("COUNT_BOOKS_ASYNC", count, "Books counted asynchronously")
        );
    }

    @Async
    public CompletableFuture<RuslanJulayevAsyncDto> countAuthorsAndReviewsAsync() {
        log.info("Async process started: count authors and reviews");
        long count = authorRepository.count() + reviewRepository.count();
        return CompletableFuture.completedFuture(
                new RuslanJulayevAsyncDto("COUNT_AUTHORS_AND_REVIEWS_ASYNC", count, "Authors + reviews counted asynchronously")
        );
    }

    @Async
    public CompletableFuture<RuslanJulayevAsyncDto> countFilesAsync() {
        log.info("Async process started: count files");
        long count = fileRepository.count();
        return CompletableFuture.completedFuture(
                new RuslanJulayevAsyncDto("COUNT_FILES_ASYNC", count, "Files counted asynchronously")
        );
    }
}
