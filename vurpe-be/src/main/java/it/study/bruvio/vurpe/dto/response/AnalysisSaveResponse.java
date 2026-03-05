package it.study.bruvio.vurpe.dto.response;

public record AnalysisSaveResponse(
        String analysisId,
        MetricsResponse metrics
) {}
