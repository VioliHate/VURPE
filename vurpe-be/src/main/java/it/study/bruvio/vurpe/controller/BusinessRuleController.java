package it.study.bruvio.vurpe.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.study.bruvio.vurpe.dto.criteria.BusinessRuleFilter;
import it.study.bruvio.vurpe.dto.response.BusinessRuleResponse;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.service.BusinessRuleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/call")
public class BusinessRuleController {

    @Autowired
    private BusinessRuleService brServ;

    @GetMapping("/rules")
    public ResponseEntity<PayloadResponse<Page<BusinessRuleResponse>>> search(
            @ModelAttribute BusinessRuleFilter criteria,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<BusinessRuleResponse> page = brServ.search(criteria, pageable).map(BusinessRuleResponse::fromEntity);
        PayloadResponse<Page<BusinessRuleResponse>> response = PayloadResponse.success(page, "Search completed");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rules/add")
    public ResponseEntity<PayloadResponse<String>> addBusinessRule(
            @RequestBody BusinessRuleResponse newBusinessRuleResponse) {
        try {
            brServ.addBusinessRule(newBusinessRuleResponse);
            PayloadResponse<String> response = PayloadResponse.success("", "Business rule added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PayloadResponse<String> response = PayloadResponse.error("",
                    "Error adding business rule: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/business-rule/delete")
    public ResponseEntity<PayloadResponse<String>> delete(
            @RequestParam("id") String id) throws Exception {
        UUID recordId = UUID.fromString(id);
        try {
            if (brServ.delete(recordId)) {
                PayloadResponse<String> response = PayloadResponse.success(null,
                        "deleted completed");
                return ResponseEntity.ok(response);
            }
            PayloadResponse<String> response = PayloadResponse.error("error on delete ", "");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(PayloadResponse.error(e.getMessage(), "error on delete"));
        }
    }

}
