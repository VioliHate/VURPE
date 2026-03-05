package it.study.bruvio.vurpe.dto.response;

import it.study.bruvio.vurpe.entity.AnalysisResult;

import java.math.BigDecimal;
import java.util.Map;

public record MetricsResponse(
        String file_id,
        BigDecimal total_amount,
        Integer record_count,
        BigDecimal average_amount,
        Map<String, Integer> distribution_by_category,
        Map<String, Integer> distribution_by_risk_flag,
        Map<String, BigDecimal> time_series_by_date
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
