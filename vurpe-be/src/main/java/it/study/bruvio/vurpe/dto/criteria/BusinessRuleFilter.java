package it.study.bruvio.vurpe.dto.criteria;

import java.util.UUID;

public record BusinessRuleFilter(

        UUID id,
        String ruleName,
        String ruleCondition,
        String riskFlag,
        Integer severity) {
    public boolean isEmpty() {
        return id == null && ruleName == null && ruleCondition == null &&
                riskFlag == null && severity == null;
    }
}
