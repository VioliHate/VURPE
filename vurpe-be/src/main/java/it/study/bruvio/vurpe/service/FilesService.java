package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.FilesFilter;
import it.study.bruvio.vurpe.entity.FileStatusEnum;
import it.study.bruvio.vurpe.entity.Files;
import it.study.bruvio.vurpe.repository.AnalysisResultRepository;
import it.study.bruvio.vurpe.repository.AsyncTaskRepository;
import it.study.bruvio.vurpe.repository.DataRecordRepository;
import it.study.bruvio.vurpe.repository.FilesRepository;
import it.study.bruvio.vurpe.specifications.FilesSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilesService {
    private final FilesRepository filesRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final AsyncTaskRepository asyncTaskRepository;
    private final DataRecordRepository dataRecordRepository;


    public Page<Files> search(FilesFilter filter, Pageable pageable) {
        Specification<Files> spec = FilesSpecifications.fromFilter(filter);
        return filesRepository.findAll(spec, pageable);
    }

    public FileStatusEnum getFileStatus(String fileId) {
        return filesRepository.getFileStatusById(UUID.fromString(fileId));
    }

    @Transactional
    public boolean delete(UUID id) throws Exception {
        if (!filesRepository.existsById(id)) {
            throw new Exception("File not exists!");
        }

        analysisResultRepository.deleteByFileId(id);
        dataRecordRepository.deleteByFileId(id);
        asyncTaskRepository.deleteByFileId(id);

        filesRepository.deleteById(id);
        return !filesRepository.existsById(id);

    }
}
