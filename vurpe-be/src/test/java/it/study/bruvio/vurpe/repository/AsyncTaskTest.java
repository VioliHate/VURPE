package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.AsyncTask;
import it.study.bruvio.vurpe.entity.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    @Test
    void shouldRejectInvalidDataTypes() {
        // Test 1: file_id null (NOT NULL)
        assertThrows(Exception.class, () -> {
            AsyncTask task = new AsyncTask();
            task.setFile_id(null); // INVALIDO
            task.setStatus(TaskStatus.QUEUED);
            repository.saveAndFlush(task);
        });

        // Test 2: status null (NOT NULL)
        assertThrows(Exception.class, () -> {
            AsyncTask task = new AsyncTask();
            task.setFile_id(UUID.randomUUID());
            task.setStatus(null); // INVALIDO
            repository.saveAndFlush(task);
        });

        // Test 3: created_at null (NOT NULL) - ma @PrePersist lo setta automaticamente
        // Questo test potrebbe non fallire per via del @PrePersist
    }

    @Test
    void shouldAcceptValidDataTypes() {
        assertDoesNotThrow(() -> {
            AsyncTask task = new AsyncTask();
            task.setFile_id(UUID.randomUUID());
            task.setStatus(TaskStatus.QUEUED);
            task.setError_message("Test error");
            task.setCompleted_at(LocalDateTime.now());
            // created_at viene settato automaticamente da @PrePersist

            AsyncTask saved = repository.saveAndFlush(task);
            assertNotNull(saved.getId());
            assertNotNull(saved.getCreated_at()); // Verifica @PrePersist
        });
    }

    @Test
    void shouldAcceptNullableFields() {
        assertDoesNotThrow(() -> {
            AsyncTask task = new AsyncTask();
            task.setFile_id(UUID.randomUUID());
            task.setStatus(TaskStatus.COMPLETED);
            task.setError_message(null); // Nullable
            task.setCompleted_at(null); // Nullable

            AsyncTask saved = repository.saveAndFlush(task);
            assertNotNull(saved.getId());
            assertNull(saved.getError_message());
            assertNull(saved.getCompleted_at());
        });
    }

    @Test
    void shouldAutoSetCreatedAt() {
        assertDoesNotThrow(() -> {
            AsyncTask task = new AsyncTask();
            task.setFile_id(UUID.randomUUID());
            task.setStatus(TaskStatus.PROCESSING);

            AsyncTask saved = repository.saveAndFlush(task);
            assertNotNull(saved.getCreated_at()); // @PrePersist deve averlo settato
        });
    }

}
