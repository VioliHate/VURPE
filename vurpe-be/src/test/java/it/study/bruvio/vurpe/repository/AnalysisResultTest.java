package it.study.bruvio.vurpe.repository;


import it.study.bruvio.vurpe.entity.AnalysisResult;
import it.study.bruvio.vurpe.entity.AsyncTask;
import it.study.bruvio.vurpe.entity.TaskStatus;
import jakarta.validation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Test AnalysisResult repo")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnalysisResultTest     {

    @Autowired
    private TestEntityManager   entityManager;

    @Autowired
    private AnalysisResultRepository repository;

    @Test
    @DisplayName("All records must have non-null ID and create_at")
    void everyRecordMustHaveNonNullIdAndCreatedAt() {
        List<AnalysisResult> allRecords=repository.findAll();

        assertThat(allRecords)
                .as("There must be at least some test records")
                .isNotEmpty();

        allRecords.forEach(AnalysisRes -> {
            assertThat(AnalysisRes.getId())
                    .as("ID must be not null for record: "+AnalysisRes)
                    .isNotNull();

            assertThat(AnalysisRes.getCreated_at())
                    .as("created_at must be not null for record: "+AnalysisRes)
                    .isNotNull();

        });
    }
    @Test
    void shouldRejectInvalidIdType() {
        // Test 1: file_id not null
        AnalysisResult analysisResult = createAnalysisResult();
        analysisResult.setFile_id(null);
        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(analysisResult);
        });
    }

    @Test
    void shouldRejectInvalidCreatedAtType() {
        // Test 2: Validation Test for created_at null
        AnalysisResult analysisResult = createAnalysisResult();
        analysisResult.setCreated_at(null);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<AnalysisResult>> violations = validator.validate(analysisResult);
            assertThat(violations)
                    .as("There must be exactly one violation for @NotNull on created_at")
                    .hasSize(1);
            assertThat(violations)
                    .extracting(ConstraintViolation::getPropertyPath)
                    .extracting(Path::toString)
                    .containsExactly("created_at");

        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }



    @Test
    void shouldAcceptValidDataTypes() {
            AnalysisResult result = createAnalysisResult();
            result.setTotal_amount(new BigDecimal("5000.50"));
            result.setRecord_count(100);
            result.setAverage_amount(new BigDecimal("50.00"));
            result.setDistribution_by_category(Map.of("Dev", 50, "QA", 30));
            result.setDistribution_by_risk_flag(Map.of("HIGH", 10, "LOW", 90));
            result.setTime_series_by_date(Map.of("2024-01", new BigDecimal("1000")));
        assertDoesNotThrow(() -> {
            AnalysisResult saved = repository.saveAndFlush(result);
            assertNotNull(saved.getId());
        });
    }

    private AnalysisResult createAnalysisResult() {
        AnalysisResult analysisResult = new AnalysisResult();
        analysisResult.setFile_id(UUID.fromString("27d0e7dd-93d5-4ce2-8212-16b3fff35163"));
        analysisResult.setTotal_amount(new BigDecimal("1000"));


        return analysisResult;

    }

}


