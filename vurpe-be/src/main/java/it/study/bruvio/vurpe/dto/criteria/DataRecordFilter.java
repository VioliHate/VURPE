package it.study.bruvio.vurpe.dto.criteria;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DataRecordFilter(
    BigDecimal amount,
    String  category,
    LocalDateTime date,
    String riskFlag,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)

{
    public boolean isEmpty(){
        return amount == null && category == null && date == null && riskFlag == null && updatedAt == null;

    }
}
