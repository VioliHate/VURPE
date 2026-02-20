package it.study.bruvio.vurpe.repository;


import it.study.bruvio.vurpe.entity.AnalysisResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    @DisplayName("Tutti i record id e create_at devono essere NON nulli")
    void everyRecordMustHaveNonNullIdAndCreatedAt() {
        List<AnalysisResult> allRecords=repository.findAll();

        assertThat(allRecords)
                .as("non devono esserci id o record_at nulli ")
                .isNotEmpty();

        allRecords.forEach(AnalysisRes -> {
            assertThat(AnalysisRes.getId())
                    .as("ID non deve essere nullo: "+AnalysisRes)
                    .isNotNull();

            assertThat(AnalysisRes.getCreated_at())
                    .as("Created_at non deve essere nullo: "+AnalysisRes)
                    .isNotNull();

        });
    }
    @Test
    void shouldRejectInvalidDataTypes() {
        // Test 1: fileId null (NOT NULL)
        assertThrows(Exception.class, () -> {
            AnalysisResult result = new AnalysisResult();
            result.setFile_id(null); // INVALIDO
            result.setTotal_amount(new BigDecimal("1000"));
            result.setRecord_count(10);
            repository.saveAndFlush(result);
        });

        // Test 2: createdAt null (NOT NULL)
        assertThrows(Exception.class, () -> {
            AnalysisResult result = new AnalysisResult();
            result.setFile_id(UUID.randomUUID());
            result.setTotal_amount(new BigDecimal("1000"));
            result.setRecord_count(10);
            result.setCreated_at(null); // INVALIDO
            repository.saveAndFlush(result);
        });
    }

    @Test
    void shouldAcceptValidDataTypes() {
        assertDoesNotThrow(() -> {
            AnalysisResult result = new AnalysisResult();
            result.setFile_id(UUID.randomUUID());
            result.setTotal_amount(new BigDecimal("5000.50"));
            result.setRecord_count(100);
            result.setAverage_amount(new BigDecimal("50.00"));
            result.setDistribution_by_category(Map.of("Dev", 50, "QA", 30));
            result.setDistribution_by_risk_flag(Map.of("HIGH", 10, "LOW", 90));
            result.setTime_series_by_date(Map.of("2024-01", new BigDecimal("1000")));
            result.setCreated_at(LocalDateTime.now());

            AnalysisResult saved = repository.saveAndFlush(result);
            assertNotNull(saved.getId());
        });
    }

    @Test
    void shouldAcceptNullableFields() {
        assertDoesNotThrow(() -> {
            AnalysisResult result = new AnalysisResult();
            result.setFile_id(UUID.randomUUID());
            // Tutti gli altri campi sono nullable, quindi null è ok
            result.setTotal_amount(null);
            result.setRecord_count(null);
            result.setAverage_amount(null);
            result.setDistribution_by_category(null);
            result.setDistribution_by_risk_flag(null);
            result.setTime_series_by_date(null);
            result.setCreated_at(LocalDateTime.now());

            AnalysisResult saved = repository.saveAndFlush(result);
            assertNotNull(saved.getId());
        });
    }


}


