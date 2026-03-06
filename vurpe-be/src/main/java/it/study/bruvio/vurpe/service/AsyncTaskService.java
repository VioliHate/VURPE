package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.AsyncTaskFilter;
import it.study.bruvio.vurpe.dto.response.AsyncTaskResponse;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.entity.*;
import it.study.bruvio.vurpe.repository.AsyncTaskRepository;
import it.study.bruvio.vurpe.repository.FilesRepository;
import it.study.bruvio.vurpe.specifications.AsyncTaskSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncTaskService {

    private final AsyncTaskRepository repoAsyncTask;
    private final IntelligenceService servInt;
    private final FilesRepository reposFiles;
    private final SimpMessagingTemplate messagingTemplate;


    public Page<AsyncTask> search(AsyncTaskFilter filter, Pageable pageable) {

        Specification<AsyncTask> spec = AsyncTaskSpecifications.fromFilter(filter);
        return repoAsyncTask.findAll(spec, pageable);
    }

    public AsyncTaskResponse getTask(UUID taskUUID) throws Exception {
        try {
            AsyncTask result = repoAsyncTask.getReferenceById(taskUUID);
            return AsyncTaskResponse.fromEntity(result);
        } catch (Exception e) {
            throw new Exception("task not exists!", e);
        }
    }


    @Async("taskExecutor")
    public void processAnalysisTask(String fileId) throws Exception {

        // check id
        if (fileId == null || fileId.isEmpty()) {
            throw new IllegalArgumentException("Missing file ID");
        }
        UUID fileUUID;
        try {
            fileUUID = UUID.fromString(fileId);
        } catch (IllegalArgumentException e) {
            this.sendUpdate(fileId, PayloadResponse.error("File ID not valid", "FILE_ID_NOT_VALID"));
            throw new IllegalArgumentException("UUID not valid: " + fileId);
        }

        Files file = reposFiles.getReferenceById(fileUUID);
        //check file
        if (file == null) {
            this.sendUpdate(fileId, PayloadResponse.error("file not exist", "FILE_NOT_FOUND"));
            throw new IllegalArgumentException("File not Exist for id: " + fileId);
        }
        if (!file.getStatus().equals(FileStatusEnum.UPLOADED)) {
            this.sendUpdate(fileId, PayloadResponse.error(
                    "Unable to start analysis on file: "+ file.getId() +": current status = " + file.getStatus(),
                    "INVALID_FILE_STATUS"
            ));
        } else {
            file.setStatus(FileStatusEnum.WORKING);
            reposFiles.saveAndFlush(file);
            this.sendUpdate(fileId, PayloadResponse.success(
                    "Processing Analysis for file: " + file.getId(),
                    "WORKING_ON_FILE"
            ));
            UUID taskId = this.queueAnalysisTask(fileUUID);
            AsyncTask asyncTask = repoAsyncTask.getReferenceById(taskId);
            try {
                asyncTask.setStatus(TaskStatus.PROCESSING);
                repoAsyncTask.saveAndFlush(asyncTask);
                sendUpdate(fileId, PayloadResponse.success(
                        "Processing Analysis Task (taskId: " + taskId + ")",
                        "PROCESSING_TASK"
                ));
                servInt.applyBusinessRulesToFile(asyncTask.getFile_id());
                asyncTask.setStatus(TaskStatus.COMPLETED);
                asyncTask.setCompleted_at(LocalDateTime.now());
                repoAsyncTask.saveAndFlush(asyncTask);
                sendUpdate(fileId, PayloadResponse.success(
                        "Completed Analysis Task (taskId: " + taskId + ")",
                        "COMPLETED_TASK"
                ));
                file.setStatus(FileStatusEnum.COMPLETED);
                reposFiles.save(file);

                this.sendUpdate(fileId, PayloadResponse.success(
                        "Analysis Complete for file: " + file.getId(),
                        "COMPLETED_ANALYSIS"
                ));
            } catch (Exception e) {
                asyncTask.setStatus(TaskStatus.FAILED);
                asyncTask.setCompleted_at(LocalDateTime.now());
                asyncTask.setError_message(e.getMessage());
                repoAsyncTask.saveAndFlush(asyncTask);
                file.setStatus(FileStatusEnum.ERROR);
                reposFiles.save(file);
                this.sendUpdate(fileId, PayloadResponse.error("Error: " + e.getMessage(), "FAILED"));
                throw new RuntimeException(e);
            }
        }
    }


    protected UUID queueAnalysisTask(UUID fileId) {

        AsyncTask asyncTask = new AsyncTask();
        asyncTask.setFile_id(fileId);
        asyncTask.setStatus(TaskStatus.QUEUED);
        AsyncTask saved = repoAsyncTask.saveAndFlush(asyncTask);
        return saved.getId();
    }

    public void sendUpdate(String fileId, PayloadResponse<String> response) {
        String destination = "/topic/analysis-task/" + fileId;
        log.info(">>> INVIO SU DESTINATION: [{}]", destination);
        messagingTemplate.convertAndSend(
                destination,
                response
        );
    }
}
