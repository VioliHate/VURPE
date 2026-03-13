package it.study.bruvio.vurpe.specifications;

import it.study.bruvio.vurpe.dto.criteria.BusinessRuleFilter;
import it.study.bruvio.vurpe.entity.BusinessRule;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class BusinessRuleSpecification {
    private BusinessRuleSpecification() {
    }

    public static Specification<BusinessRule> fromFilter(BusinessRuleFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.isEmpty()) {
                return cb.conjunction();
            }
            if (filter.id() != null) {
                predicates.add(cb.equal(root.get("id"), filter.id()));
            }
            if (filter.ruleName() != null) {
                predicates.add(cb.equal(root.get("ruleName"), filter.ruleName()));
            }
            if (filter.ruleCondition() != null) {
                predicates.add(cb.equal(root.get("ruleCondition"), filter.ruleCondition()));
            }
            if (filter.riskFlag() != null) {
                predicates.add(cb.equal(root.get("riskFlag"), filter.riskFlag()));
            }
            if (filter.severity() != null) {
                predicates.add(cb.equal(root.get("severity"), filter.severity()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));

        };
    }

}
