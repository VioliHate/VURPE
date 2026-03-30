package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.dto.response.StatusCountDTO;
import it.study.bruvio.vurpe.entity.FileStatusEnum;
import it.study.bruvio.vurpe.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FilesRepository extends JpaRepository<Files, UUID>, JpaSpecificationExecutor<Files> {
    boolean existsById(UUID fileId);

    Files getReferenceById(UUID uuiDid);

    @Query("SELECT f.status FROM Files f WHERE f.id = :id")
    FileStatusEnum getFileStatusById(@Param("id") UUID id);

    @Query("SELECT new it.study.bruvio.vurpe.dto.response.StatusCountDTO(f.status, COUNT(f)) " +
            "FROM Files f GROUP BY f.status")
    List<StatusCountDTO> countFilesByStatus();

    void deleteBy(UUID id);
}
