package it.study.bruvio.vurpe.repository;


import it.study.bruvio.vurpe.entity.AnalysisResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
}


