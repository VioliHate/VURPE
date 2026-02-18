package it.study.bruvio.vurpe.dto.response;



import it.study.bruvio.vurpe.entity.AnalysisResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record AnalysisResultResponse (
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

    public static  AnalysisResultResponse fromEntity(AnalysisResult E) {
       return new AnalysisResultResponse(
               E.getId(),
               E.getFile_id(),
               E.getTotal_amount(),
               E.getRecord_count(),
               E.getAverage_amount(),
               E.getDistribution_by_category(),
               E.getDistribution_by_risk_flag(),
               E.getTime_series_by_date(),
               E.getCreated_at ()

       );
    }
}
