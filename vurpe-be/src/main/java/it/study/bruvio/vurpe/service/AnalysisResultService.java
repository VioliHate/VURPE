package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.AnalysisResultFilter;
import it.study.bruvio.vurpe.entity.AnalysisResult;
import it.study.bruvio.vurpe.repository.AnalysisResultRepository;
import it.study.bruvio.vurpe.specifications.AnalysisResultSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisResultService {

    private final AnalysisResultRepository repository;

    public Page<AnalysisResult> search(AnalysisResultFilter filter, Pageable pageable) {

        Specification<AnalysisResult> spec = AnalysisResultSpecifications.fromFilter(filter);
        return repository.findAll(spec, pageable);
    }
}
