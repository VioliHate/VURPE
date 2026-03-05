package it.study.bruvio.vurpe.dto.response;

import it.study.bruvio.vurpe.entity.AnalysisResult;

import java.math.BigDecimal;
import java.util.Map;

public record MetricsResponse(
        String fileId,
        BigDecimal totalAmount,
        Integer recordCount,
        BigDecimal averageAmount,
        Map<String, Integer> distributionByCategory,
        Map<String, Integer> distributionByRiskFlag,
        Map<String, BigDecimal> timeSeriesByDate
) {

    public static MetricsResponse from(AnalysisResult entity) {
        return new MetricsResponse(
                entity.getFile_id().toString(),
                entity.getTotal_amount(),
                entity.getRecord_count(),
                entity.getAverage_amount(),
                entity.getDistribution_by_category(),
                entity.getDistribution_by_risk_flag(),
                entity.getTime_series_by_date()
        );
    }
}
