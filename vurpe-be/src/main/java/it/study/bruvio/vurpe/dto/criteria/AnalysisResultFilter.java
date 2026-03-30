package it.study.bruvio.vurpe.dto.criteria;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record AnalysisResultFilter (
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

    public boolean isEmpty(){
        return (id==null && fileId==null && totalAmount==null && recordCount==null && averageAmount==null
        && distributionByCategory==null && distributionByRiskFlag==null && timeSeriesByDate==null
                && createdAt==null) ;
    }
}
