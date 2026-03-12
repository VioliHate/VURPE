package it.study.bruvio.vurpe.dto.response;

import it.study.bruvio.vurpe.entity.Files;

import java.time.Instant;

public record FilesResponse(
        String originalName,
        Long fileSize,
        String status,
        Instant createdAt
) {
    public static FilesResponse fromEntity(Files entity) {
        return new FilesResponse(
                entity.getOriginalName(),
                entity.getFileSize(),
                entity.getStatus().name(),
                entity.getCreatedAt()
        );
    }
}
