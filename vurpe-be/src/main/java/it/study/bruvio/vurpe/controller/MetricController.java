package it.study.bruvio.vurpe.controller;





import it.study.bruvio.vurpe.dto.Request.QueryRequest;
import it.study.bruvio.vurpe.dto.response.QueryResult;
import it.study.bruvio.vurpe.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
//esempio json da postman
// {
//    "field": "amount",
//    "operation": "SUM",
//    "groupBy": "category",
//    "limit": 10,
//    "filters": []
//}
public class MetricController {

    private final MetricService srv;

    @PostMapping("/metrics")
    public ResponseEntity<QueryResult> executeCustomQuery(@RequestBody QueryRequest request) {
        // Esegue la query e ritorna il risultato
        return ResponseEntity.ok(srv.executeQuery(request));
    }
}