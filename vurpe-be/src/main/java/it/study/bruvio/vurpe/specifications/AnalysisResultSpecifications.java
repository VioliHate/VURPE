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
            if(filter.average_amount()!=null){
                predicates.add(cb.equal(root.get("average_amount"),filter.average_amount()));
            }
            if(filter.created_at() !=null){
                predicates.add(cb.equal(root.get("created_at"),filter.created_at()));
            }
            if(filter.distribution_by_category() !=null){
                predicates.add(cb.equal(root.get("distribution_by_category"),filter.distribution_by_category()));
            }
            if(filter.file_id() !=null){
                predicates.add(cb.equal(root.get("file_id"),filter.file_id()));
            }
            if(filter.time_series_by_date() !=null){
                predicates.add(cb.equal(root.get("time_series_by_date"),filter.time_series_by_date()));
            }
            if(filter.total_amount() != null){
                predicates.add(cb.equal(root.get("total_amount"),filter.total_amount()));
            }
            if(filter.distribution_by_risk_flag() != null){
                predicates.add(cb.equal(root.get("distribution_by_risk_flag"),filter.distribution_by_risk_flag()));
            }
            if(filter.record_count() != null){
                predicates.add(cb.equal(root.get("record_count"),filter.record_count()));
            }
            return cb.and(predicates.toArray(Predicate[]::new));

        };
    }
}
