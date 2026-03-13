package it.study.bruvio.vurpe.repository;

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

    @Query("SELECT d FROM DataRecord d WHERE d.file_id = :fileId")
    List<DataRecord> findByFileId(UUID fileId);

    @Query("SELECT SUM(d.amount) FROM DataRecord d where d.file_id = :fileId")
    BigDecimal sumAmountByFileId(UUID fileId);

    @Query("SELECT AVG(d.amount) FROM DataRecord d where d.file_id = :fileId")
    BigDecimal avgAmountByFileId(UUID fileId);

    @Query("SELECT COUNT(d) FROM DataRecord d where d.file_id = :fileId")
    Integer countByFileId(UUID fileId);

    @Query("SELECT d.category,count(d) FROM DataRecord d WHERE d.file_id = :fileId GROUP BY d.category")
    List<Object[]> countByCategoryRaw(UUID fileId);

    @Query("SELECT d.risk_flag,count(d) FROM DataRecord d WHERE d.file_id = :fileId GROUP BY d.risk_flag")
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

}
