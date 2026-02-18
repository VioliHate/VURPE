package it.study.bruvio.vurpe.dto.criteria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record AnalysisResultFilter (
    UUID id  ,
    UUID file_id ,
    BigDecimal total_amount ,
    Integer record_count ,
    BigDecimal average_amount ,
    Map<String, Integer> distribution_by_category ,
    Map<String, Integer> distribution_by_risk_flag ,
    Map<String, BigDecimal> time_series_by_date ,
    LocalDateTime created_at

    ){

    public boolean isEmpty(){
        return (id==null && file_id==null && total_amount==null && record_count==null && average_amount==null
        && distribution_by_category==null && distribution_by_risk_flag==null && time_series_by_date==null
                && created_at==null) ;
    }
}
