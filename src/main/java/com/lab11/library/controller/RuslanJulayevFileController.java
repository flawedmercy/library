package com.lab11.library.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.lab11.library.dto.RuslanJulayevFileDto;
import com.lab11.library.entity.RuslanJulayevFileEntity;
import com.lab11.library.service.RuslanJulayevFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class RuslanJulayevFileController {
    private final RuslanJulayevFileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileService.upload(file));
    }

    @GetMapping
    public ResponseEntity<List<RuslanJulayevFileDto.Response>> getAll() {
        return ResponseEntity.ok(fileService.findAll());
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> download(@PathVariable Long id) {
        RuslanJulayevFileEntity file = fileService.getFile(id);
        ByteArrayResource resource = new ByteArrayResource(file.getData());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFileName() + "\"")
                .contentLength(file.getSizeBytes())
                .body(resource);
    }
}
