package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.AsyncTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("test AsyncTask repo")
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class AsyncTaskTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AsyncTaskRepository repository;

    @Test
    @DisplayName("tutti i record devono avere id e create_at non nulli")
    void everyRecordMushHaveNonNullIdAndCreateAt(){
        List<AsyncTask> allRecords=repository.findAll();

        assertThat(allRecords)
                .as("controllo che il db non sia vuoto")
                .isNotEmpty();

        allRecords.forEach(i -> {
            assertThat(i.getId())
                    .as("ID non deve essere nullo: "+i)
            .isNotNull();

            assertThat(i.getCreated_at())
                    .as("createdAt non deve essere nullo: "+i)
                    .isNotNull();
        });





    }
}
