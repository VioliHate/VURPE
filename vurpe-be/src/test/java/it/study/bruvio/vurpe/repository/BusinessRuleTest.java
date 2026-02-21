package it.study.bruvio.vurpe.repository;


import it.study.bruvio.vurpe.entity.BusinessRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("TestBusinessRule repo")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BusinessRuleTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BusinessRuleRepository repository;

    @Test
    @DisplayName("All records must have non-null ID and create_at")
    void everyRecordMustHaveNonNullIdAndCreatedAt() {
        // take all record
        List<BusinessRule> allRecords = repository.findAll();

        assertThat(allRecords)
                .as("There must be at least some test records")
                .isNotEmpty();

        allRecords.forEach(record -> {
            assertThat(record.getId())
                    .as("ID must be not null for record: " + record)
                    .isNotNull();


        });
    }

    @Test
    void shouldRejectInvalidRuleNameTypes() {
        // Test 1: rule_name null (NOT NULL)
        BusinessRule businessRule = createBusinessRule("HIGH", 1);
        businessRule.setRule_name(null);
        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(businessRule);
        });
    }

    @Test
    void shouldRejectInvalidRuleConditionTypes() {
        // Test 2: rule_condition null (NOT NULL)
        BusinessRule businessRule = createBusinessRule("HIGH", 2);
        businessRule.setRule_condition(null);
        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(businessRule);
        });
    }

    @Test
    void shouldRejectInvalidRiskFlagTypes() {
        // Test 3: risk_flag null (NOT NULL)
        BusinessRule businessRule = createBusinessRule("MEDIUM", 3);
        businessRule.setRisk_flag(null);
        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(businessRule);
        });
    }

    @Test
    void shouldRejectInvalidSeverityTypes() {
        // Test 4: severity null (NOT NULL)
        BusinessRule businessRule = createBusinessRule("LOW", 3);
        businessRule.setSeverity(null);
        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(businessRule);
        });
    }

    @Test
    void shouldAcceptValidDataTypes() {
        assertDoesNotThrow(() -> {
            BusinessRule rule = createBusinessRule("HIGH", 5);
            rule.setRule_name("High Amount Alert");
            BusinessRule saved = repository.saveAndFlush(rule);
            assertNotNull(saved.getId());
            assertEquals("High Amount Alert", saved.getRule_name());
            assertEquals(5, saved.getSeverity());
        });
    }

    private BusinessRule createBusinessRule(String risk_flag, int severity) {
        BusinessRule rule = new BusinessRule();
        rule.setRule_name("rule-test");
        rule.setRule_condition("amount >1000");
        rule.setRisk_flag(risk_flag);
        rule.setSeverity(severity);

        return rule;
    }
}
