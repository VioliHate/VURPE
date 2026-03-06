package it.study.bruvio.vurpe.controller;


import it.study.bruvio.vurpe.dto.criteria.AsyncTaskFilter;
import it.study.bruvio.vurpe.dto.response.AsyncTaskResponse;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping(path = "/call")
public class AsyncTaskController {

    @Autowired
    private AsyncTaskService asyncTaskService;


    @GetMapping("/async-tasks")
    public ResponseEntity<PayloadResponse<Page<AsyncTaskResponse>>> search(
            @ModelAttribute AsyncTaskFilter criteria,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<AsyncTaskResponse> page = asyncTaskService.search(criteria, pageable).map(AsyncTaskResponse::fromEntity);
        PayloadResponse<Page<AsyncTaskResponse>> response = PayloadResponse.success(page, "Search complete.");

        return ResponseEntity.ok(response);

    }

    @MessageMapping("/start-async-analyzer")
    @SendTo("/topic/analysis-task")
    public PayloadResponse<String> analyzer(
            @Payload String id
    ) throws Exception {
        System.out.println(">>> START-ASYNC-ANALYZER invocato con id: " + id);
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing file ID");
        }
        UUID fileId;
        try {
            fileId = UUID.fromString(id.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UUID not valid: " + id);
        }

         asyncTaskService.processAnalysisTask(fileId);

        return PayloadResponse.success("ANALYZER STARTED FOR FILE ID: " + id,
                "ANALYSIS_TASK_QUEUED"
        );
    }

    @GetMapping("/analysis/{taskId}")
    public ResponseEntity<PayloadResponse<AsyncTaskResponse>> getTask(
            @PathVariable String taskId) {
        UUID taskUUID = UUID.fromString(taskId);
        try {
            return ResponseEntity.ok().body(PayloadResponse.success(asyncTaskService.getTask(taskUUID), "SUCCESS_TAKE_TASK"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(PayloadResponse.error(e.getMessage(), "TAKE_TASK_ERROR"));
        }
    }
}
