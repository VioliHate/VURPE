package it.study.bruvio.vurpe.dto.response;

import java.util.UUID;

public record BusinessRuleResponse(
        UUID id,
        String ruleName,
        String ruleCondition,
        String riskFlag,
        Integer severity) {
    public static BusinessRuleResponse fromEntity(it.study.bruvio.vurpe.entity.BusinessRule entity) {
        return new BusinessRuleResponse(
                entity.getId(),
                entity.getRuleName(),
                entity.getRuleCondition(),
                entity.getRiskFlag(),
                entity.getSeverity());
    }

}
