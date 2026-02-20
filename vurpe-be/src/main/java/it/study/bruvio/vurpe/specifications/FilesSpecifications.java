package it.study.bruvio.vurpe.specifications;

import it.study.bruvio.vurpe.dto.criteria.FilesFilter;
import it.study.bruvio.vurpe.entity.Files;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FilesSpecifications {
    private FilesSpecifications() {
    }
    public static Specification<Files> fromFilter(FilesFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.isEmpty()){
                return cb.conjunction();
            }
            if(filter.id() != null){
                predicates.add(cb.equal(root.get("id"), filter.id()
                ));
            }
            if(filter.original_name() != null){
                predicates.add(cb.equal(root.get("original_name"), filter.original_name()
                ));
            }
            if(filter.file_size() != null){
                predicates.add(cb.equal(root.get("file_size"), filter.file_size()
                ));
            }
            if(filter.upload_status() != null){
                predicates.add(cb.equal(root.get("upload_status"), filter.upload_status()
                ));
            }
            if(filter.created_at() != null){
                predicates.add(cb.equal(root.get("created_at"), filter.created_at()
                ));
            }

            return cb.and(predicates.toArray(Predicate[]::new));

        };
    }
}
