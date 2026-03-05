package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.AnalysisResultFilter;
import it.study.bruvio.vurpe.dto.response.MetricsResponse;
import it.study.bruvio.vurpe.entity.AnalysisResult;
import it.study.bruvio.vurpe.repository.AnalysisResultRepository;
import it.study.bruvio.vurpe.repository.DataRecordRepository;
import it.study.bruvio.vurpe.repository.FilesRepository;
import it.study.bruvio.vurpe.specifications.AnalysisResultSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisResultService {

    private final AnalysisResultRepository repoAnalysis;

    private final DataRecordRepository repoDataRecord;

    private final FilesRepository repoFiles;



    public Page<AnalysisResult> search(AnalysisResultFilter filter, Pageable pageable) {

        Specification<AnalysisResult> spec = AnalysisResultSpecifications.fromFilter(filter);
        return repoAnalysis.findAll(spec, pageable);
    }

    public AnalysisResult saveAnalysisResult(UUID fileID) throws Exception {

        AnalysisResult analysisResult = new AnalysisResult();
        if(!repoFiles.existsById(fileID)) {
            throw new Exception("File not exists!");
        }
        try {
            analysisResult.setFile_id(fileID);
            analysisResult.setTotal_amount(repoDataRecord.sumAmountByFileId(fileID));
            analysisResult.setRecord_count(repoDataRecord.countByFileId(fileID));
            analysisResult.setAverage_amount(repoDataRecord.avgAmountByFileId(fileID));
            analysisResult.setDistribution_by_category(this.mapToCount(repoDataRecord.countByCategoryRaw(fileID)));
            analysisResult.setDistribution_by_risk_flag(this.mapToCount(repoDataRecord.countByRiskFlagRaw(fileID)));
            analysisResult.setTime_series_by_date(this.mapToDailySum(repoDataRecord.sumAmountTimeSeriesByDate(fileID)));
            return repoAnalysis.save(analysisResult);
        }catch (Exception e){
            throw new Exception("Error to create analysis record", e);
        }
    }


    public MetricsResponse getMetrics(UUID analysisId) throws Exception {
        try {
            Optional<AnalysisResult> result = repoAnalysis.findById(analysisId);
            return result.map(MetricsResponse::from).get();
        } catch (Exception e) {
            throw new Exception("metrics not exists!",e);
        }
    }

    //utility mapper
    protected Map<String, Integer> mapToCount(List<Object[]> rows) {
        return rows.stream().collect(Collectors.toMap(
                r -> (String) r[0],
                r  -> {
                    Long cnt = (Long) r[1];
                    if (cnt > Integer.MAX_VALUE || cnt < Integer.MIN_VALUE) {
                        throw new ArithmeticException("COUNT to big for: " + cnt);
                    }
                    return cnt.intValue();
                },
                (o,n) -> o,
                LinkedHashMap::new
        ));
    }

    protected Map<String, BigDecimal> mapToDailySum(List<Object[]> rows) {
        return rows.stream().collect(Collectors.toMap(
                r ->  r[0].toString(),
                r -> (BigDecimal) r[1],
                (o,n) -> o,
                LinkedHashMap::new
        ));
    }

}
