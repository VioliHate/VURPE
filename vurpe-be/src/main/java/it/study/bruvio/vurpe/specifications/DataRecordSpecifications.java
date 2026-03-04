package it.study.bruvio.vurpe.specifications;

import io.micrometer.common.util.StringUtils;
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

            if (filter.file_id() != null) {
                predicates.add(cb.equal(root.get("file_id"), filter.file_id()));
            }

            if (StringUtils.isNotBlank(filter.category())) {
                String cat = filter.category().trim();
                if (cat.contains("%") || cat.contains("_")) {
                    predicates.add(cb.like(root.get("category"), cat));
                } else {
                    predicates.add(cb.equal(root.get("category"), cat));
                }
            }

            if (StringUtils.isNotBlank(filter.risk_flag())) {
                String rf = filter.risk_flag().trim();
                if (rf.contains("%") || rf.contains("_")) {
                    predicates.add(cb.like(root.get("risk_flag"), rf));
                } else {
                    predicates.add(cb.equal(root.get("risk_flag"), rf));
                }
            }

            // (search) - su più campi
            if (StringUtils.isNotBlank(filter.search())) {
                String pattern = "%" + filter.search().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("description")), pattern),
                        cb.like(cb.lower(root.get("original_id")), pattern),
                        cb.like(cb.lower(root.get("category")), pattern),
                        cb.like(cb.lower(root.get("risk_flag")), pattern)
                ));
            }

            if (filter.min_amount() != null) {
                predicates.add(cb.ge(root.get("amount"), filter.min_amount()));
            }
            if (filter.max_amount() != null) {
                predicates.add(cb.le(root.get("amount"), filter.max_amount()));
            }
            if (filter.date_from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.date_from()));
            }
            if (filter.date_to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.date_to()));
            }
            if (filter.id() != null) {
                predicates.add(cb.equal(root.get("id"), filter.id()));
            }
            if (filter.exact_date() != null) {
                predicates.add(cb.equal(root.get("date"), filter.exact_date()));
            }
            if (filter.created_at_from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("created_at"), filter.created_at_from()));
            }
            if (filter.created_at_to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("created_at"), filter.created_at_to()));
            }
            if (filter.updated_at_from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("updated_at"), filter.updated_at_from()));
            }
            if (filter.updated_at_to() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("updated_at"), filter.updated_at_to()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
