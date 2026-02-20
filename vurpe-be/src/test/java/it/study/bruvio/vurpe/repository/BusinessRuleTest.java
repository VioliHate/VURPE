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
    @DisplayName("All records must have non-null IDs and create_at")
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
    void shouldRejectInvalidDataTypes() {
        // Test 1: rule_name null (NOT NULL)
        assertThrows(Exception.class, () -> {
            BusinessRule rule = new BusinessRule();
            rule.setRule_name(null); // INVALIDO
            rule.setRule_condition("amount > 1000");
            rule.setRisk_flag("HIGH");
            rule.setSeverity(3);
            repository.saveAndFlush(rule);
        });

        // Test 2: rule_condition null (NOT NULL)
        assertThrows(Exception.class, () -> {
            BusinessRule rule = new BusinessRule();
            rule.setRule_name("High Amount Rule");
            rule.setRule_condition(null); // INVALIDO
            rule.setRisk_flag("HIGH");
            rule.setSeverity(3);
            repository.saveAndFlush(rule);
        });

        // Test 3: risk_flag null (NOT NULL)
        assertThrows(Exception.class, () -> {
            BusinessRule rule = new BusinessRule();
            rule.setRule_name("High Amount Rule");
            rule.setRule_condition("amount > 1000");
            rule.setRisk_flag(null); // INVALIDO
            rule.setSeverity(3);
            repository.saveAndFlush(rule);
        });

        // Test 4: severity null (NOT NULL)
        assertThrows(Exception.class, () -> {
            BusinessRule rule = new BusinessRule();
            rule.setRule_name("High Amount Rule");
            rule.setRule_condition("amount > 1000");
            rule.setRisk_flag("HIGH");
            rule.setSeverity(null); // INVALIDO
            repository.saveAndFlush(rule);
        });
    }

    @Test
    void shouldAcceptValidDataTypes() {
        assertDoesNotThrow(() -> {
            BusinessRule rule = new BusinessRule();
            rule.setRule_name("High Amount Alert");
            rule.setRule_condition("amount > 10000");
            rule.setRisk_flag("HIGH");
            rule.setSeverity(5);

            BusinessRule saved = repository.saveAndFlush(rule);
            assertNotNull(saved.getId());
            assertEquals("High Amount Alert", saved.getRule_name());
            assertEquals(5, saved.getSeverity());
        });
    }
}
