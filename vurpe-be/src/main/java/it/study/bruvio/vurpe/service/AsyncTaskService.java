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
import java.util.concurrent.CompletableFuture;

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
           throw new Exception("task not exists!",e);
        }
    }

    public PayloadResponse<String> requestAnalysis(UUID fileId) {

        Files file = reposFiles.getReferenceById(fileId);

        if (file == null) {
            return PayloadResponse.error("file not exist", "FILE_NOT_FOUND");
        }

        if (!file.getStatus().equals(FileStatusEnum.UPLOADED)) {
            return PayloadResponse.error(
                    "Unable to start analysis: current status = " + file.getStatus(),
                    "INVALID_FILE_STATUS"
            );
        }

        // Se arriviamo qui → possiamo avviare
        queueAnalysisAsync(fileId);

        return PayloadResponse.success(
                "Queued analysis for files: " + fileId,
                "ANALYSIS_TASK_QUEUED"
        );
    }
    @Async("taskExecutor")
    public void queueAnalysisAsync(UUID fileId) {
        try {
            Files file = reposFiles.getReferenceById(fileId);
            UUID taskId = queueAnalysisTask(fileId);

            file.setStatus(FileStatusEnum.WORKING);
            reposFiles.saveAndFlush(file);

            AsyncTask task = repoAsyncTask.getReferenceById(taskId);
            task.setStatus(TaskStatus.PROCESSING);
            repoAsyncTask.saveAndFlush(task);

            servInt.applyBusinessRulesToFile(fileId);

            task.setStatus(TaskStatus.COMPLETED);
            task.setCompleted_at(LocalDateTime.now());
            repoAsyncTask.saveAndFlush(task);

            file.setStatus(FileStatusEnum.COMPLETED);
            reposFiles.saveAndFlush(file);

            sendUpdate(fileId, PayloadResponse.success(
                    "Analysis completed",
                    "ANALYSIS_COMPLETED"
            ));

        } catch (Exception e) {
            Files file = reposFiles.getReferenceById(fileId);
            file.setStatus(FileStatusEnum.ERROR);
            reposFiles.saveAndFlush(file);

            sendUpdate(fileId, PayloadResponse.error(
                    "Error while parsing analysis: " + e.getMessage(),
                    "ANALYSIS_FAILED"
            ));
        }
    }



    protected UUID queueAnalysisTask(UUID fileId)  {

        AsyncTask asyncTask=new AsyncTask();
        asyncTask.setFile_id(fileId);
        asyncTask.setStatus(TaskStatus.QUEUED);
        AsyncTask saved= repoAsyncTask.saveAndFlush(asyncTask);
        return saved.getId();
    }

  /*  @Async("taskExecutor")
    public void processAnalysisTask(UUID fileId) throws Exception {

        if(!reposFiles.existsById(fileId)) {
            throw new Exception("File not exists!");
        }

        Files file = reposFiles.getReferenceById(fileId);

        if(file.getStatus().equals(FileStatusEnum.COMPLETED)){
            CompletableFuture.completedFuture(PayloadResponse.success("analysis is already completed!", "OK"));
            return;
        }

        if(file.getStatus().equals(FileStatusEnum.UPLOADED)) {
            UUID taskId = this.queueAnalysisTask(fileId);
            file.setStatus(FileStatusEnum.WORKING);
            reposFiles.saveAndFlush(file);
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

                messagingTemplate.convertAndSend(
                        "/topic/analysis-task",
                        PayloadResponse.success("analysis completed for file: " + fileId, "COMPLETED")
                );

                CompletableFuture.completedFuture(PayloadResponse.success("analysis completed!", "OK"));
                return;
            } catch (Exception e) {
                asyncTask.setStatus(TaskStatus.FAILED);
                asyncTask.setCompleted_at(LocalDateTime.now());
                asyncTask.setError_message(e.getMessage());
                repoAsyncTask.saveAndFlush(asyncTask);
                file.setStatus(FileStatusEnum.ERROR);
                reposFiles.save(file);
                messagingTemplate.convertAndSend(
                        "/topic/analysis-task",
                        PayloadResponse.error("Error: " + e.getMessage(), "FAILED")
                );
                throw new RuntimeException(e);
            }
        }

        CompletableFuture.completedFuture(PayloadResponse.error("unprocessed file: status is " + file.getStatus().name(), "KO"));

    }*/

    private void sendUpdate(UUID fileId, PayloadResponse<String> response) {
        messagingTemplate.convertAndSend(
                "/topic/analysis-task/" + fileId,
                response
        );
    }

}
