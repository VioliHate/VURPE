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
            if(filter.file_id() != null){
                predicates.add(cb.equal(root.get("file_id"), filter.file_id()));
            }
            if(filter.status() != null){
                predicates.add(cb.equal(root.get("status"), filter.status().name()));
            }
            if(filter.error_message() != null && !filter.error_message().trim().isEmpty()){
                String pattern = "%" + filter.error_message().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("error_message")),
                                pattern.toLowerCase()));
            }
            if(filter.created_at() != null){
                predicates.add(cb.equal(root.get("created_at"), filter.created_at()));
            }
            if(Boolean.TRUE.equals(filter.not_completed_at())){
                predicates.add(cb.isNull(root.get("completed_at")));
            } else if(filter.completed_at() != null){
                predicates.add(cb.equal(root.get("completed_at"), filter.completed_at()));
            }


            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
