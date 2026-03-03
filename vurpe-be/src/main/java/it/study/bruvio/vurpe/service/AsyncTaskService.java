package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.AsyncTaskFilter;
import it.study.bruvio.vurpe.entity.AsyncTask;
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

@Service
@RequiredArgsConstructor
public class AsyncTaskService {

    private final AsyncTaskRepository repository;
    private final IntelligenceService IsService;
    private final FilesRepository filesRepo;


    public Page<AsyncTask> search(AsyncTaskFilter filter, Pageable pageable) {

        Specification<AsyncTask> spec = AsyncTaskSpecifications.fromFilter(filter);
        return repository.findAll(spec, pageable);
    }

    public UUID queueAnalysisTask(UUID fileId)  {

        AsyncTask asyncTask=new AsyncTask();
        asyncTask.setFile_id(fileId);
        asyncTask.setStatus(TaskStatus.QUEUED);
        AsyncTask saved=repository.save(asyncTask);
        return saved.getId();
    }

    @Async("taskExecutor")
    public void  processAnalysisTask(UUID taskId){
        AsyncTask asyncTask = repository.getReferenceById(taskId);
        try {

            asyncTask.setStatus(TaskStatus.PROCESSING);
            repository.save(asyncTask);
            IsService.applyBusinessRulesToFile(asyncTask.getFile_id());
            asyncTask.setStatus(TaskStatus.COMPLETED);
            asyncTask.setCompleted_at(LocalDateTime.now());
            repository.save(asyncTask);

        } catch (Exception e) {
            asyncTask.setStatus(TaskStatus.FAILED);
            asyncTask.setCompleted_at(LocalDateTime.now());
            repository.save(asyncTask);
            throw new RuntimeException(e);
        }


    }
}
