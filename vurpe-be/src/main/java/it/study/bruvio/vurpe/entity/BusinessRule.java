package it.study.bruvio.vurpe.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "business_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Column(name = "rule_condition", nullable = false)
    private String ruleCondition;

    @Column(name = "risk_flag", nullable = false)
    private String riskFlag;

    @Column(name = "severity", nullable = false)
    private Integer severity;
}
