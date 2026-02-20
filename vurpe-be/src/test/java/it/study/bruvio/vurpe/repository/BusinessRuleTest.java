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
}
