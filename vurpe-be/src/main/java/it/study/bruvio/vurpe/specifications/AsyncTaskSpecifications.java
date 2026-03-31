package it.study.bruvio.vurpe.specifications;

import it.study.bruvio.vurpe.dto.criteria.AsyncTaskFilter;
import it.study.bruvio.vurpe.entity.AsyncTask;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskSpecifications {
    private AsyncTaskSpecifications(){}
    public static Specification<AsyncTask> fromFilter(AsyncTaskFilter filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.isEmpty()) {
                return cb.conjunction();
            }
            if(filter.id() != null){
                predicates.add(cb.equal(root.get("id"), filter.id()
                ));
            }
            if(filter.fileId() != null){
                predicates.add(cb.equal(root.get("fileId"), filter.fileId()));
            }
            if(filter.status() != null){
                predicates.add(cb.equal(root.get("status"), filter.status().name()));
            }
            if(filter.errorMessage() != null && !filter.errorMessage().trim().isEmpty()){
                String pattern = "%" + filter.errorMessage().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("errorMessage")),
                                pattern.toLowerCase()));
            }
            if(filter.createdAt() != null){
                predicates.add(cb.equal(root.get("createdAt"), filter.createdAt()));
            }
            if(Boolean.TRUE.equals(filter.notCompletedAt())){
                predicates.add(cb.isNull(root.get("completedAt")));
            } else if(filter.completedAt() != null){
                predicates.add(cb.equal(root.get("completedAt"), filter.completedAt()));
            }


            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
