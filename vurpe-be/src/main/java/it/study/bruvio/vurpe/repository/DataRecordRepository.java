package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.DataRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRecordRepository extends JpaRepository<DataRecord, Integer>, JpaSpecificationExecutor<DataRecord> {
}
