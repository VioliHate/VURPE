package it.study.bruvio.vurpe.controller;

import it.study.bruvio.vurpe.dto.criteria.FilesFilter;
import it.study.bruvio.vurpe.dto.response.FilesResponse;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.entity.FileStatusEnum;
import it.study.bruvio.vurpe.service.FilesService;
import it.study.bruvio.vurpe.service.IngestionService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "/call")
public class FilesController {

    @Autowired
    private FilesService filesService;
    @Autowired
    private IngestionService ingServ;

    @GetMapping("/files")
    public ResponseEntity<PayloadResponse<Page<FilesResponse>>> search(
            @ModelAttribute FilesFilter criteria,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<FilesResponse> page = filesService.search(criteria, pageable).map(FilesResponse::fromEntity);
        PayloadResponse<Page<FilesResponse>> response = PayloadResponse.success(page, "Search completed");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/files/delete")
    public ResponseEntity<PayloadResponse<String>> delete(
            @RequestParam("id") String id) throws Exception {
        UUID RecordId = UUID.fromString(id);
        try {
            boolean res = filesService.delete(RecordId);
            if (res) {
                PayloadResponse<String> response = PayloadResponse.success(null,
                        "deleted completed");
                return ResponseEntity.ok(response);
            }
            PayloadResponse<String> response = PayloadResponse.error("errore in delete ", "");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(PayloadResponse.error(e.getMessage(), "error in delete"));
        }
    }

    @PostMapping("/uploadCSV")
    public ResponseEntity<PayloadResponse<String>> uploadFile(
            @RequestParam("file") MultipartFile file) {
        try {
            PayloadResponse<String> pr = ingServ.uploadFile(file);
            return ResponseEntity.ok(pr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(PayloadResponse.error(e.getMessage(), "CSV_PARSE_ERROR"));
        }
    }

    @GetMapping("upload/{fileId}/status")
    public ResponseEntity<PayloadResponse<FileStatusEnum>> getFilesStatus(@PathVariable String fileId) {
        PayloadResponse<FileStatusEnum> response = PayloadResponse.success(filesService.getFileStatus(fileId),
                "success response status file");
        return ResponseEntity.ok(response);
    }
}
