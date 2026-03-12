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
            if(filter.originalName() != null){
                predicates.add(cb.equal(root.get("original_name"), filter.originalName()
                ));
            }
            if(filter.fileSize() != null){
                predicates.add(cb.equal(root.get("file_size"), filter.fileSize()
                ));
            }
            if(filter.status() != null){
                predicates.add(cb.equal(root.get("upload_status"), filter.status()
                ));
            }
            if(filter.createdAt() != null){
                predicates.add(cb.equal(root.get("createdAt"), filter.createdAt()
                ));
            }

            return cb.and(predicates.toArray(Predicate[]::new));

        };
    }
}
