package it.study.bruvio.vurpe.dto.criteria;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DataRecordFilter(
    BigDecimal amount,
    String  category,
    LocalDateTime date,
    String risk_flag,
    LocalDateTime created_at,
    LocalDateTime updated_at)

{
    public boolean isEmpty(){
        return amount == null && category == null && date == null && risk_flag == null && updated_at == null;

    }
}
