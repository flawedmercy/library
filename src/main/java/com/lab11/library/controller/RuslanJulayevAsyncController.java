package com.lab11.library.controller;

import com.lab11.library.dto.RuslanJulayevAsyncDto;
import com.lab11.library.service.RuslanJulayevAsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/async")
@RequiredArgsConstructor
public class RuslanJulayevAsyncController {
    private final RuslanJulayevAsyncService asyncService;

    @GetMapping("/books/count")
    public CompletableFuture<ResponseEntity<RuslanJulayevAsyncDto>> countBooks() {
        return asyncService.countBooksAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/authors-reviews/count")
    public CompletableFuture<ResponseEntity<RuslanJulayevAsyncDto>> countAuthorsAndReviews() {
        return asyncService.countAuthorsAndReviewsAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/files/count")
    public CompletableFuture<ResponseEntity<RuslanJulayevAsyncDto>> countFiles() {
        return asyncService.countFilesAsync().thenApply(ResponseEntity::ok);
    }
}
