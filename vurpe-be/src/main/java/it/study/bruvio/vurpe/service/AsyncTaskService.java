package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.AsyncTaskFilter;
import it.study.bruvio.vurpe.dto.response.AsyncTaskResponse;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.entity.*;
import it.study.bruvio.vurpe.repository.AsyncTaskRepository;
import it.study.bruvio.vurpe.repository.FilesRepository;
import it.study.bruvio.vurpe.specifications.AsyncTaskSpecifications;
import lombok.RequiredArgsConstructor;
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
    public void processAnalysisTask(UUID fileId) throws Exception {
        Files file = reposFiles.getReferenceById(fileId);

        if (this.checkFile(file)) {
            UUID taskId = this.queueAnalysisTask(fileId);
            AsyncTask asyncTask = repoAsyncTask.getReferenceById(taskId);
            try {
                asyncTask.setStatus(TaskStatus.PROCESSING);
                repoAsyncTask.saveAndFlush(asyncTask);
                servInt.applyBusinessRulesToFile(asyncTask.getFile_id());
                asyncTask.setStatus(TaskStatus.COMPLETED);
                asyncTask.setCompleted_at(LocalDateTime.now());
                repoAsyncTask.saveAndFlush(asyncTask);
                file.setStatus(FileStatusEnum.COMPLETED);
                reposFiles.save(file);

                this.sendUpdate(file, PayloadResponse.success(
                        "Analysis Complete for file: " + file.getOriginal_name(),
                        "COMPLETED_ANALYSIS"
                ));
            } catch (Exception e) {
                asyncTask.setStatus(TaskStatus.FAILED);
                asyncTask.setCompleted_at(LocalDateTime.now());
                asyncTask.setError_message(e.getMessage());
                repoAsyncTask.saveAndFlush(asyncTask);
                file.setStatus(FileStatusEnum.ERROR);
                reposFiles.save(file);
                this.sendUpdate(file, PayloadResponse.error("Error: " + e.getMessage(), "FAILED"));
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

    private void sendUpdate(Files file, PayloadResponse<String> response) {
        messagingTemplate.convertAndSend(
                "/topic/analysis-task/" + file,
                response
        );
    }

    private boolean checkFile(Files file) {

        if (file == null) {
            this.sendUpdate(null, PayloadResponse.error("file not exist", "FILE_NOT_FOUND"));
            return false;
        }

        if (!file.getStatus().equals(FileStatusEnum.UPLOADED)) {
            this.sendUpdate(file, PayloadResponse.error(
                    "Unable to start analysis: current status = " + file.getStatus(),
                    "INVALID_FILE_STATUS"
            ));
            return false;

        }
        file.setStatus(FileStatusEnum.WORKING);
        reposFiles.saveAndFlush(file);
        this.sendUpdate(file, PayloadResponse.success(
                "Processing Analysis for file: " + file.getOriginal_name(),
                "INVALID_FILE_STATUS"
        ));
        return true;

    }

}
