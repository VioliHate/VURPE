package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.FilesFilter;
import it.study.bruvio.vurpe.entity.Files;
import it.study.bruvio.vurpe.repository.FilesRepository;
import it.study.bruvio.vurpe.specifications.FilesSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilesService {
    private final FilesRepository filesRepository;

    public Page<Files> search(FilesFilter filter, Pageable pageable) {
        Specification<Files> spec = FilesSpecifications.fromFilter(filter);
        return filesRepository.findAll(spec, pageable);
    }
}
