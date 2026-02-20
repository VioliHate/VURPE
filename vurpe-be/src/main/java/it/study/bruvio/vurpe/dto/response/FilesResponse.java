package it.study.bruvio.vurpe.dto.response;

import it.study.bruvio.vurpe.entity.Files;

import java.time.Instant;
import java.util.UUID;

public record FilesResponse(
        UUID id,
        String original_name,
        Long file_size,
        String upload_status,
        Instant created_at
) {
    public static FilesResponse fromEntity(Files entity) {
        return new FilesResponse(
                entity.getId(),
                entity.getOriginal_name(),
                entity.getFile_size(),
                entity.getUpload_status(),
                entity.getCreated_at()
        );
    }
}
