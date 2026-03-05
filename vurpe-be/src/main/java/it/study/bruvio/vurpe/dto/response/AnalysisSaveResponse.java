package it.study.bruvio.vurpe.dto.response;

public record AnalysisSaveResponse(
        String analysis_id,
        MetricsResponse metrics
) {}
