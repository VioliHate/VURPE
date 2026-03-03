package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.AsyncTaskFilter;
import it.study.bruvio.vurpe.dto.response.PayloadResponse;
import it.study.bruvio.vurpe.entity.AsyncTask;
import it.study.bruvio.vurpe.entity.FileStatusEnum;
import it.study.bruvio.vurpe.entity.Files;
import it.study.bruvio.vurpe.entity.TaskStatus;
import it.study.bruvio.vurpe.repository.AsyncTaskRepository;
import it.study.bruvio.vurpe.repository.FilesRepository;
import it.study.bruvio.vurpe.specifications.AsyncTaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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


    public Page<AsyncTask> search(AsyncTaskFilter filter, Pageable pageable) {

        Specification<AsyncTask> spec = AsyncTaskSpecifications.fromFilter(filter);
        return repoAsyncTask.findAll(spec, pageable);
    }

    protected UUID queueAnalysisTask(UUID fileId)  {

        AsyncTask asyncTask=new AsyncTask();
        asyncTask.setFile_id(fileId);
        asyncTask.setStatus(TaskStatus.QUEUED);
        AsyncTask saved= repoAsyncTask.saveAndFlush(asyncTask);
        return saved.getId();
    }

    @Async("taskExecutor")
    public CompletableFuture<PayloadResponse<String>> processAnalysisTask(UUID fileId) throws Exception {

        if(!reposFiles.existsById(fileId)) {
            throw new Exception("File not exists!");
        }

        Files file = reposFiles.getReferenceById(fileId);

        if(file.getStatus().equals(FileStatusEnum.COMPLETED)){
            return CompletableFuture.completedFuture(PayloadResponse.success("analysis is already completed!", "OK"));
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
                return CompletableFuture.completedFuture(PayloadResponse.success("analysis completed!", "OK"));
            } catch (Exception e) {
                asyncTask.setStatus(TaskStatus.FAILED);
                asyncTask.setCompleted_at(LocalDateTime.now());
                asyncTask.setError_message(e.getMessage());
                repoAsyncTask.saveAndFlush(asyncTask);
                file.setStatus(FileStatusEnum.ERROR);
                reposFiles.save(file);
                throw new RuntimeException(e);
            }
        }

        return CompletableFuture.completedFuture(PayloadResponse.error("unprocessed file: status is " + file.getStatus().name(), "KO"));

    }
}
