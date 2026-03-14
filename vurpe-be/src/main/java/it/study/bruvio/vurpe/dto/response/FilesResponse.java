package it.study.bruvio.vurpe.dto.response;

import it.study.bruvio.vurpe.entity.Files;

import java.time.Instant;
import java.util.UUID;

public record FilesResponse(
        UUID id,
        String originalName,
        Long fileSize,
        String status,
        Instant createdAt) {
    public static FilesResponse fromEntity(Files entity) {

        return new FilesResponse(
                entity.getId(),
                entity.getOriginalName(),
                entity.getFileSize(),
                entity.getStatus().name(),
                entity.getCreatedAt());
    }
}
