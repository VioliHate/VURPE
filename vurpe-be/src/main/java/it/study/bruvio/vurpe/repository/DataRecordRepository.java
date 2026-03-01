package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.DataRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface DataRecordRepository extends JpaRepository<DataRecord, Integer>,
        JpaSpecificationExecutor<DataRecord> {
    @Query("SELECT d FROM DataRecord d WHERE d.file_id = :fileId")
    List<DataRecord> findByFileId(UUID fileId);
}
