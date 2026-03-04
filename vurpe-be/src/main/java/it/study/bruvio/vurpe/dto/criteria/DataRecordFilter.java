package it.study.bruvio.vurpe.dto.criteria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DataRecordFilter(
        UUID file_id,
        String category,
        String risk_flag,
        String search,                  // ricerca libera su description / original_id / category
        BigDecimal min_amount,
        BigDecimal max_amount,
        LocalDateTime date_from,
        LocalDateTime date_to,

        UUID id,
        LocalDateTime exact_date,
        LocalDateTime created_at_from,
        LocalDateTime created_at_to
)

{
    public boolean isEmpty(){
        return file_id == null && category == null && risk_flag == null &&
               search == null && min_amount == null && max_amount == null &&
               date_from == null && date_to == null && id == null &&
               exact_date == null && created_at_from == null && created_at_to == null;

    }
    public static DataRecordFilter withFileId(UUID fileId){
        return new DataRecordFilter(fileId,null, null, null, null, null, null, null, null, null, null, null);
    }
}
