package it.study.bruvio.vurpe.specifications;

import it.study.bruvio.vurpe.dto.criteria.AnalysisResultFilter;
import it.study.bruvio.vurpe.entity.AnalysisResult;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResultSpecifications {
    private AnalysisResultSpecifications(){}
    public static Specification<AnalysisResult> fromFilter(AnalysisResultFilter filter){
        return (root ,query,cb) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(filter.isEmpty()){
                return cb.conjunction();
            }
            if(filter.id() !=null){
                predicates.add(cb.equal(root.get("id"),filter.id()));
            }
            if(filter.averageAmount()!=null){
                predicates.add(cb.equal(root.get("averageAmount"),filter.averageAmount()));
            }
            if(filter.createdAt() !=null){
                predicates.add(cb.equal(root.get("createdAt"),filter.createdAt()));
            }
            if(filter.distributionByCategory() !=null){
                predicates.add(cb.equal(root.get("distributionByCategory"),filter.distributionByCategory()));
            }
            if(filter.fileId() !=null){
                predicates.add(cb.equal(root.get("fileId"),filter.fileId()));
            }
            if(filter.timeSeriesByDate() !=null){
                predicates.add(cb.equal(root.get("timeSeriesByDate"),filter.timeSeriesByDate()));
            }
            if(filter.totalAmount() != null){
                predicates.add(cb.equal(root.get("totalAmount"),filter.totalAmount()));
            }
            if(filter.distributionByRiskFlag() != null){
                predicates.add(cb.equal(root.get("distributionByRiskFlag"),filter.distributionByRiskFlag()));
            }
            if(filter.recordCount() != null){
                predicates.add(cb.equal(root.get("recordCount"),filter.recordCount()));
            }
            return cb.and(predicates.toArray(Predicate[]::new));

        };
    }
}
