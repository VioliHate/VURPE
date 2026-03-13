package it.study.bruvio.vurpe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.study.bruvio.vurpe.dto.criteria.BusinessRuleFilter;
import it.study.bruvio.vurpe.dto.response.BusinessRuleResponse;
import it.study.bruvio.vurpe.entity.BusinessRule;
import it.study.bruvio.vurpe.repository.BusinessRuleRepository;
import it.study.bruvio.vurpe.specifications.BusinessRuleSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessRuleService {
    private final BusinessRuleRepository brRepo;

    public Page<BusinessRule> search(BusinessRuleFilter filter, Pageable pageable) {
        Specification<BusinessRule> spec = BusinessRuleSpecification.fromFilter(filter);
        return brRepo.findAll(spec, pageable);
    }

    public void addBusinessRule(BusinessRuleResponse req) {
        if (req.ruleName() == null || req.ruleName().isBlank()) {
            throw new IllegalArgumentException("Rule name is required");
        }

        BusinessRule rule = new BusinessRule(
                null,
                req.ruleName(),
                req.ruleCondition(),
                req.riskFlag(),
                req.severity());

        brRepo.save(rule);
    }

}
