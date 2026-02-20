package it.study.bruvio.vurpe.dto.criteria;

import java.time.Instant;
import java.util.UUID;

public record FilesFilter(
        UUID id,
        String original_name,
        Long file_size,
        String upload_status,
        Instant created_at
) {
    public boolean isEmpty() {
        return id ==null && original_name == null && file_size == null && upload_status == null &&
                created_at == null;
    }
}
