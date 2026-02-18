package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<Files, Integer>, JpaSpecificationExecutor<Files> {
}
