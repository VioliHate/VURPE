package it.study.bruvio.vurpe.controller;


import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.service.IntelligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path="/call")
public class IntelligenceController {

    @Autowired
    private IntelligenceService intelligenceService;


    @GetMapping("/start-analyzer")
    public ResponseEntity<PayloadResponse<String>> analyzer(
        @RequestParam("id") String id
        ){
        UUID UUIDid = UUID.fromString(id);
        try{
            intelligenceService.applyBusinessRulesToFile(UUIDid);
            PayloadResponse<String> response = PayloadResponse.success("Analisi avvenuta","Success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            PayloadResponse<String> response = PayloadResponse.error("errore durante l analisi", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        }
    }
}
