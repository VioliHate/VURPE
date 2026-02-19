package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.AsyncTaskFilter;
import it.study.bruvio.vurpe.entity.AsyncTask;
import it.study.bruvio.vurpe.repository.AsyncTaskRepository;
import it.study.bruvio.vurpe.specifications.AsyncTaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncTaskService {

    private final AsyncTaskRepository repository;

    public Page<AsyncTask> search(AsyncTaskFilter filter, Pageable pageable) {

        Specification<AsyncTask> spec = AsyncTaskSpecifications.fromFilter(filter);
        return repository.findAll(spec, pageable);
    }
}
