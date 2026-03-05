package it.study.bruvio.vurpe.controller;

import it.study.bruvio.vurpe.dto.criteria.AnalysisResultFilter;
import it.study.bruvio.vurpe.dto.response.AnalysisResultResponse;
import it.study.bruvio.vurpe.dto.response.MetricsResponse;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.service.AnalysisResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/call")
public class AnalysisResultController {

    @Autowired
    private AnalysisResultService service;

    @GetMapping("/analysis-results")
    public ResponseEntity<PayloadResponse<Page<AnalysisResultResponse>>> search(
            @ModelAttribute AnalysisResultFilter criteria,

            @PageableDefault(size=20 , sort="id" , direction= Sort.Direction.ASC) Pageable pageable){
        Page<AnalysisResultResponse> page = service.search(criteria,pageable)
                .map(AnalysisResultResponse::fromEntity);
        PayloadResponse<Page<AnalysisResultResponse>> response = PayloadResponse.success(page , "Search completed");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save-analysis")
    public ResponseEntity<PayloadResponse<MetricsResponse>> saveAnalysisResult(
            @RequestParam("fileId") String fileId) throws Exception {
        UUID id = UUID.fromString(fileId);
        try {
            MetricsResponse metrics = MetricsResponse.from(service.saveAnalysisResult(id));
            PayloadResponse<MetricsResponse> response = PayloadResponse.success(metrics, "Save completed");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(PayloadResponse.error(e.getMessage(), "SAVE_ANALYSIS_ERROR"));
        }
    }
}
