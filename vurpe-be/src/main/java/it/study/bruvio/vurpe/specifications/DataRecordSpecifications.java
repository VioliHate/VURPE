package it.study.bruvio.vurpe.specifications;

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

            if(filter.risk_flag() != null &&  !filter.risk_flag().isEmpty()) {
                predicates.add(cb.equal(root.get("risk_flag"), filter.risk_flag()));
            }

            if(filter.created_at() != null) {
                predicates.add(cb.equal(root.get("created_at"), filter.created_at()));
            }

            if(filter.updated_at() != null) {
                predicates.add(cb.equal(root.get("updated_at"), filter.updated_at()));
            }



            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
