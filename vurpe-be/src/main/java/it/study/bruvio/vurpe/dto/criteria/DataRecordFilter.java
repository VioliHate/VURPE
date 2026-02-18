package it.study.bruvio.vurpe.dto.criteria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DataRecordFilter(
    UUID id,
    UUID file_id,
    String original_id,
    BigDecimal amount,
    String  category,
    LocalDateTime date,
    String description,
    String risk_flag,
    LocalDateTime created_at,
    LocalDateTime updated_at)

{
    public boolean isEmpty(){
        return id == null && file_id==null && original_id==null && amount == null &&
               category == null && date == null && risk_flag == null && updated_at == null;

    }
}
