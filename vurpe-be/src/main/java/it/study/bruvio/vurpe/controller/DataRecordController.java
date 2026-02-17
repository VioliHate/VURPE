package it.study.bruvio.vurpe.controller;

import it.study.bruvio.vurpe.dto.criteria.DataRecordFilter;
import it.study.bruvio.vurpe.dto.response.DataRecordResponse;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.entity.DataRecord;
import it.study.bruvio.vurpe.service.DataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/call")
public class DataRecordController {

    @Autowired
    private DataRecordService dataRecordService;


    @GetMapping("/data-records")
    public ResponseEntity<PayloadResponse<Page<DataRecordResponse>>> search(
            @ModelAttribute DataRecordFilter criteria,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<DataRecordResponse> page = dataRecordService.search(criteria, pageable)
                .map(DataRecordResponse::fromEntity);

        PayloadResponse<Page<DataRecordResponse>> response = PayloadResponse.success(page, "Search completed");

        return ResponseEntity.ok(response);
    }
}
