package it.study.bruvio.vurpe.dto.response;

import it.study.bruvio.vurpe.entity.DataRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DataRecordResponse (
        UUID id,
        UUID fileId,
        String originalId,
        BigDecimal amount,
        String category,
        LocalDateTime date,
        String description,
        String riskFlag,
        LocalDateTime createdAt,
        LocalDateTime updatedAt){

    public static DataRecordResponse fromEntity(DataRecord entity) {
        return new DataRecordResponse(
                entity.getId(),
                entity.getFile_id(),
                entity.getOriginal_id(),
                entity.getAmount(),
                entity.getCategory(),
                entity.getDate(),
                entity.getDescription(),
                entity.getRisk_flag(),
                entity.getCreated_at(),
                entity.getUpdated_at()
        );
    }
}
