package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Integer> {
}
