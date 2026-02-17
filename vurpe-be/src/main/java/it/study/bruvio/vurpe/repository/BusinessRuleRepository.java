package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.BusinessRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRuleRepository extends JpaRepository<BusinessRule, Integer> {
}
