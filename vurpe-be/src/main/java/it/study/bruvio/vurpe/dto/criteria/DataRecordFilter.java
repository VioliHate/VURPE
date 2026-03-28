package it.study.bruvio.vurpe.dto.criteria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DataRecordFilter(
        UUID id,
        UUID fileId,
        String originalId,
        BigDecimal amount,
        String category,
        LocalDateTime date,
        String description,
        String riskFlag,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public boolean isEmpty() {
        return id == null &&
               fileId == null &&
               originalId == null &&
               amount == null &&
               category == null &&
               date == null &&
               description == null &&
               riskFlag == null &&
               createdAt == null &&
               updatedAt == null;
    }

    public static DataRecordFilter withFileId(UUID fileId) {
        return new DataRecordFilter(
                null,
                fileId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}