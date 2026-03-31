package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.dto.response.StatusCountDTO;
import it.study.bruvio.vurpe.entity.DataRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public interface DataRecordRepository extends JpaRepository<DataRecord, UUID>,
        JpaSpecificationExecutor<DataRecord> {

    Iterable<? extends UUID> findAllByFileId(UUID fileId);

    @Query("SELECT d FROM DataRecord d WHERE d.fileId = :fileId")
    List<DataRecord> findByFileId(UUID fileId);

    @Query("SELECT SUM(d.amount) FROM DataRecord d where d.fileId = :fileId")
    BigDecimal sumAmountByFileId(UUID fileId);

    @Query("SELECT AVG(d.amount) FROM DataRecord d where d.fileId = :fileId")
    BigDecimal avgAmountByFileId(UUID fileId);

    @Query("SELECT COUNT(d) FROM DataRecord d where d.fileId = :fileId")
    Integer countByFileId(UUID fileId);

    @Query("SELECT d.category,count(d) FROM DataRecord d WHERE d.fileId = :fileId GROUP BY d.category")
    List<Object[]> countByCategoryRaw(UUID fileId);

    @Query("SELECT d.riskFlag, COUNT(d) FROM DataRecord d WHERE d.fileId = :fileId GROUP BY d.riskFlag")
    List<Object[]> countByRiskFlagRaw(UUID fileId);

    @Query(value = """
                SELECT
                    date_trunc('day', d.date)::date AS day,
                    SUM(d.amount) AS total
                FROM data_records d
                WHERE d.file_id = :fileId
                GROUP BY date_trunc('day', d.date)
            """, nativeQuery = true)
    List<Object[]> sumAmountTimeSeriesByDate(UUID fileId);

    @Query("SELECT new it.study.bruvio.vurpe.dto.response.StatusCountDTO(f.riskFlag, COUNT(f)) FROM DataRecord f GROUP BY f.riskFlag")
    List<StatusCountDTO> countDataRecordsByRiskFlag();


}