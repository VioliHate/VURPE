package it.study.bruvio.vurpe.utils;

import it.study.bruvio.vurpe.dto.criteria.DataRecordFilter;
import it.study.bruvio.vurpe.entity.DataRecord;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DataRecordSpecifications {
    private DataRecordSpecifications() {}
    public static Specification<DataRecord> fromFilter(DataRecordFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.isEmpty()){
                return cb.conjunction();
            }

            if (filter.amount() != null) {
                predicates.add(cb.equal(root.get("amount"), filter.amount()
                ));
            }
            if(filter.category() != null && !filter.category().isEmpty()) {
                predicates.add(cb.equal(root.get("category"), filter.category()));
            }
            if(filter.date() != null) {
                predicates.add(cb.equal(root.get("date"), filter.date()));
            }
            if(filter.riskFlag() != null &&  !filter.riskFlag().isEmpty()) {
                predicates.add(cb.equal(root.get("riskFlag"), filter.riskFlag()));
            }
            if(filter.createdAt() != null) {
                predicates.add(cb.equal(root.get("createdAt"), filter.createdAt()));
            }
            if(filter.updatedAt() != null) {
                predicates.add(cb.equal(root.get("updatedAt"), filter.updatedAt()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
