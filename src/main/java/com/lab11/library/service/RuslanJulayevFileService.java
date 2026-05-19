package com.lab11.library.service;

import com.lab11.library.dto.RuslanJulayevFileDto;
import com.lab11.library.entity.RuslanJulayevFileEntity;
import com.lab11.library.exception.ResourceNotFoundException;
import com.lab11.library.repository.RuslanJulayevFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuslanJulayevFileService {
    private final RuslanJulayevFileRepository fileRepository;

    @Transactional
    public RuslanJulayevFileDto.Response upload(MultipartFile file) {
        try {
            RuslanJulayevFileEntity saved = fileRepository.save(RuslanJulayevFileEntity.builder()
                    .originalFileName(file.getOriginalFilename())
                    .contentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType())
                    .sizeBytes(file.getSize())
                    .data(file.getBytes())
                    .uploadedAt(LocalDateTime.now())
                    .build());

            log.info("Uploaded file id={} name={} size={}", saved.getId(), saved.getOriginalFileName(), saved.getSizeBytes());
            return toResponse(saved);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not upload file: " + ex.getMessage(), ex);
        }
    }

    public List<RuslanJulayevFileDto.Response> findAll() {
        return fileRepository.findAll().stream().map(this::toResponse).toList();
    }

    public RuslanJulayevFileEntity getFile(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File", id));
    }

    private RuslanJulayevFileDto.Response toResponse(RuslanJulayevFileEntity f) {
        return RuslanJulayevFileDto.Response.builder()
                .id(f.getId())
                .originalFileName(f.getOriginalFileName())
                .contentType(f.getContentType())
                .sizeBytes(f.getSizeBytes())
                .uploadedAt(f.getUploadedAt())
                .downloadUrl("/api/files/" + f.getId() + "/download")
                .build();
    }
}
