package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.AnalysisResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, UUID>,
        JpaSpecificationExecutor<AnalysisResult> {

    Optional<AnalysisResult> findById(UUID analysisResultId);


}
