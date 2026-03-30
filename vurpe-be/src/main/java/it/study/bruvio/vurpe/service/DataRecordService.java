package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.DataRecordFilter;
import it.study.bruvio.vurpe.entity.DataRecord;
import it.study.bruvio.vurpe.repository.DataRecordRepository;
import it.study.bruvio.vurpe.specifications.DataRecordSpecifications;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DataRecordService {
    private final DataRecordRepository repository;

    public Page<DataRecord> search(DataRecordFilter filter, Pageable pageable) {
        Specification<DataRecord> spec = DataRecordSpecifications.fromFilter(filter);

        return repository.findAll(spec, pageable);
    }

    @Transactional
    public boolean delete(UUID id) throws Exception {
        if (!repository.existsById(id)) {
            throw new Exception("File not exists!");
        }
        repository.deleteById(id);
        if (!repository.existsById(id)) {
            return true;
        }
        return false;

    }

}
