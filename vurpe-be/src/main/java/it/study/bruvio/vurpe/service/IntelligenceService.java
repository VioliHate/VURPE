package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.dto.criteria.DataRecordFilter;
import it.study.bruvio.vurpe.entity.BusinessRule;
import it.study.bruvio.vurpe.entity.DataRecord;
import it.study.bruvio.vurpe.repository.BusinessRuleRepository;
import it.study.bruvio.vurpe.repository.DataRecordRepository;
import it.study.bruvio.vurpe.specifications.DataRecordSpecifications;
import it.study.bruvio.vurpe.specifications.FilesSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.logging.Filter;

@Service
@RequiredArgsConstructor
public class IntelligenceService {
    private final BusinessRuleRepository businessRuleRepository;
    private final DataRecordRepository dataRecordRepository;

    @Transactional
    public void applyBusinessRulesToFile(UUID fileId) {
        // 1. Recupera tutti i record del file
        DataRecordFilter filter = DataRecordFilter.withFileId(fileId);
        Specification<DataRecord> spec = DataRecordSpecifications.fromFilter(filter);
        List<DataRecord> records = dataRecordRepository.findAll(spec);
        try {

            // 2. Applica le regole a tutti i record
            applyBusinessRules(records);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    protected void applyBusinessRules(List<DataRecord> records) {
        List<BusinessRule> allRules = businessRuleRepository.findAll();
        try {
            for (DataRecord record : records) {
                BusinessRule matchedRule = findBestMatchingRule(record, allRules);
                if (matchedRule != null) {
                    record.setRisk_flag(matchedRule.getRisk_flag());

                }
            }


            dataRecordRepository.saveAll(records);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BusinessRule findBestMatchingRule(DataRecord record, List<BusinessRule> allRules) {


        BusinessRule bestRule = null;
        int highestSeverity = 0;


        for (BusinessRule rule : allRules) {

            boolean matches = evaluateRule(record, rule);

            if (matches) {

                if (rule.getSeverity() > highestSeverity) {
                    highestSeverity = rule.getSeverity();
                    bestRule = rule;
                }
            }
        }


        return bestRule;
    }


    private boolean evaluateRule(DataRecord record, BusinessRule rule) {
        String condition = rule.getRule_condition();

        try {

            if (condition.equals("amount > 10000 AND category = 'transfer'")) {
                return record.getAmount().compareTo(new BigDecimal("10000")) > 0
                        && "transfer".equalsIgnoreCase(record.getCategory());
            }


            if (condition.equals("amount > 5000 AND amount <= 10000 AND category = 'transfer'")) {
                BigDecimal amount = record.getAmount();
                return amount.compareTo(new BigDecimal("5000")) > 0
                        && amount.compareTo(new BigDecimal("10000")) <= 0
                        && "transfer".equalsIgnoreCase(record.getCategory());
            }


            if (condition.equals("category = 'internal'")) {
                return "internal".equalsIgnoreCase(record.getCategory());
            }

            return false;

        } catch (Exception e) {
            System.err.println("Error evaluating rule: " + e.getMessage());
            return false;
        }
    }


}
