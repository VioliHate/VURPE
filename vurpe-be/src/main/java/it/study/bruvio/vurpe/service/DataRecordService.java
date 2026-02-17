package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.DataRecordFilter;
import it.study.bruvio.vurpe.entity.DataRecord;
import it.study.bruvio.vurpe.repository.DataRecordRepository;
import it.study.bruvio.vurpe.utils.DataRecordSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataRecordService {
    private final DataRecordRepository repository;

    public Page<DataRecord> search(DataRecordFilter filter, Pageable pageable) {
        Specification<DataRecord> spec = DataRecordSpecifications.fromFilter(filter);
        return repository.findAll(spec, pageable);
    }
}
