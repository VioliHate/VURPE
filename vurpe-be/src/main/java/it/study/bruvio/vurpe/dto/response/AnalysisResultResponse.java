package it.study.bruvio.vurpe.dto.response;



import it.study.bruvio.vurpe.entity.AnalysisResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record AnalysisResultResponse (
        UUID id  ,
        UUID fileId ,
        BigDecimal totalAmount ,
        Integer recordCount ,
        BigDecimal averageAmount ,
        Map<String, Integer> distributionByCategory ,
        Map<String, Integer> distributionByRiskFlag ,
        Map<String, BigDecimal> timeSeriesByDate ,
        LocalDateTime createdAt

){

    public static  AnalysisResultResponse fromEntity(AnalysisResult E) {
       return new AnalysisResultResponse(
               E.getId(),
               E.getFileId(),
               E.getTotalAmount(),
               E.getRecordCount(),
               E.getAverageAmount(),
               E.getDistributionByCategory(),
               E.getDistributionByRiskFlag(),
               E.getTimeSeriesByDate(),
               E.getCreatedAt ()

       );
    }
}
