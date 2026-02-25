package it.study.bruvio.vurpe.controller;


import it.study.bruvio.vurpe.dto.response.FilesResponse;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.service.IntelligenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path="/call")
public class IntelligenceController {

    @Autowired
    private IntelligenceService intelligenceService;


    @GetMapping("/ciao")
    public ResponseEntity<PayloadResponse<String>> analizer(
        @ModelAttribute String id
        ){
        UUID UUIDid=UUID.fromString(id);
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
