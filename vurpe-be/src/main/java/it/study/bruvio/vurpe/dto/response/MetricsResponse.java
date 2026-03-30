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
                entity.getFileId().toString(),
                entity.getTotalAmount(),
                entity.getRecordCount(),
                entity.getAverageAmount(),
                entity.getDistributionByCategory(),
                entity.getDistributionByRiskFlag(),
                entity.getTimeSeriesByDate()
        );
    }
}
