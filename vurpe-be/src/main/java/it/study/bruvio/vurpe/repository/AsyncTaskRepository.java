package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.dto.response.StatusCountDTO;
import it.study.bruvio.vurpe.entity.AsyncTask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AsyncTaskRepository extends JpaRepository<AsyncTask, UUID>,
                JpaSpecificationExecutor<AsyncTask> {
        AsyncTask getReferenceById(UUID taskId);

        @Query("SELECT new it.study.bruvio.vurpe.dto.response.StatusCountDTO(a.status, COUNT(a)) " +
                        "FROM AsyncTask a GROUP BY a.status")
        List<StatusCountDTO> countTasksByStatus();
        Iterable<? extends UUID> findAllByFileId(UUID fileId);

}
