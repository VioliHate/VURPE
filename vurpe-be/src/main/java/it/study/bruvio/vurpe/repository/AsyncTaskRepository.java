package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.AsyncTask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AsyncTaskRepository extends JpaRepository<AsyncTask, Integer>,
        JpaSpecificationExecutor<AsyncTask> {
    AsyncTask getReferenceById(UUID taskId);
}
