package it.study.bruvio.vurpe.dto.response;

import it.study.bruvio.vurpe.entity.DataRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DataRecordResponse (
        UUID id,
        UUID file_id,
        String original_id,
        BigDecimal amount,
        String category,
        LocalDateTime date,
        String description,
        String risk_flag,
        LocalDateTime created_at,
        LocalDateTime updated_at
){

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
