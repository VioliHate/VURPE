package it.study.bruvio.vurpe.dto.criteria;

import it.study.bruvio.vurpe.entity.FileStatusEnum;

import java.time.Instant;
import java.util.UUID;

public record FilesFilter(
        UUID id,
        String originalName,
        Long fileSize,
        FileStatusEnum status,
        Instant createdAt
) {
    public boolean isEmpty() {
        return id ==null && originalName == null && fileSize == null && status == null &&
                createdAt == null;
    }
}
