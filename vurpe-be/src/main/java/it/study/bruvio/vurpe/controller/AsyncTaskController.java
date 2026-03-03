package it.study.bruvio.vurpe.controller;


import it.study.bruvio.vurpe.dto.criteria.AsyncTaskFilter;
import it.study.bruvio.vurpe.dto.response.AsyncTaskResponse;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.entity.FileStatusEnum;
import it.study.bruvio.vurpe.entity.Files;
import it.study.bruvio.vurpe.repository.FilesRepository;
import it.study.bruvio.vurpe.service.AsyncTaskService;
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
public class AsyncTaskController {

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Autowired
    private FilesRepository filesRepo;

    @GetMapping("/async-tasks")
    public ResponseEntity<PayloadResponse<Page<AsyncTaskResponse>>> search(
            @ModelAttribute AsyncTaskFilter criteria,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){

        Page<AsyncTaskResponse> page = asyncTaskService.search(criteria, pageable).map(AsyncTaskResponse::fromEntity);
        PayloadResponse<Page<AsyncTaskResponse>> response = PayloadResponse.success(page,"Search complete.");

        return ResponseEntity.ok(response);

    }

    @GetMapping("/start-async-analyzer")
    public ResponseEntity<PayloadResponse<String>> analyzer(
            @RequestParam("id") String id
    ){
        UUID UUIDid = UUID.fromString(id);
        if(!filesRepo.existsById(UUIDid)){
            PayloadResponse<String> response = PayloadResponse.error("file non esiste",null );
            return ResponseEntity.badRequest().body(response);

        }
        Files f=filesRepo.getReferenceById(UUIDid);
            if(f.getStatus().equals(FileStatusEnum.COMPLETED)){
                PayloadResponse<String> response = PayloadResponse.success("Analisi già avvenuta " ,"Success");
                return ResponseEntity.ok(response);
            }
            else if(f.getStatus().equals(FileStatusEnum.UPLOADED)) {
            try {
                UUID taskAsyncUUID = asyncTaskService.queueAnalysisTask(UUIDid);
                f.setStatus(FileStatusEnum.WORKING);
                filesRepo.saveAndFlush(f);
                asyncTaskService.processAnalysisTask(taskAsyncUUID);
                f.setStatus(FileStatusEnum.COMPLETED);
                filesRepo.save(f);
                PayloadResponse<String> response = PayloadResponse.success("Analisi avvenuta", "Success");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                f.setStatus(FileStatusEnum.ERROR);
                filesRepo.save(f);
                PayloadResponse<String> response = PayloadResponse.error("errore durante l analisi", e.getMessage());
                return ResponseEntity.badRequest().body(response);

            }
        }
        PayloadResponse<String> response = PayloadResponse.error("errore",null );
        return ResponseEntity.badRequest().body(response);
    }
}
