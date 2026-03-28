package it.study.bruvio.vurpe.specifications;

import io.micrometer.common.util.StringUtils;
import it.study.bruvio.vurpe.dto.criteria.DataRecordFilter;
import it.study.bruvio.vurpe.entity.DataRecord;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DataRecordSpecifications {
    private DataRecordSpecifications() {
    }

    public static Specification<DataRecord> fromFilter(DataRecordFilter filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.isEmpty()) {
                return cb.conjunction();
            }

            if (filter.id() != null) {
                predicates.add(cb.equal(root.get("id"), filter.id()));
            }

            if (filter.fileId() != null) {
                predicates.add(cb.equal(root.get("fileId"), filter.fileId()));
            }

            if (filter.originalId() != null) {
                predicates.add(cb.equal(root.get("OriginalId"), filter.originalId()));
            }

            if (StringUtils.isNotBlank(filter.category())) {
                String cat = filter.category().trim();
                if (cat.contains("%") || cat.contains("_")) {
                    predicates.add(cb.like(root.get("category"), cat));
                } else {
                    predicates.add(cb.equal(root.get("category"), cat));
                }
            }

            if (StringUtils.isNotBlank(filter.riskFlag())) {
                String rf = filter.riskFlag().trim();
                if (rf.contains("%") || rf.contains("_")) {
                    predicates.add(cb.like(root.get("risk_flag"), rf));
                } else {
                    predicates.add(cb.equal(root.get("risk_flag"), rf));
                }
            }
            if (filter.createdAt() != null) {
                predicates.add(cb.equal(root.get("createdAt"), filter.createdAt()));
            }
            if (filter.updatedAt() != null) {
                predicates.add(cb.equal(root.get("updateAt"), filter.updatedAt()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
