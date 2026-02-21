package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.AsyncTask;
import it.study.bruvio.vurpe.entity.Files;
import it.study.bruvio.vurpe.entity.TaskStatus;
import jakarta.validation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    @DisplayName("All records must have non-null ID and create_at")
    void everyRecordMushHaveNonNullIdAndCreateAt(){
        List<AsyncTask> allRecords=repository.findAll();

        assertThat(allRecords)
                .as("There must be at least some test records")
                .isNotEmpty();

        allRecords.forEach(i -> {
            assertThat(i.getId())
                    .as("ID must be not null for record: "+i)
            .isNotNull();

            assertThat(i.getCreated_at())
                    .as("created_at must be not null for record: "+i)
                    .isNotNull();
        });





    }
    @Test
    void shouldRejectInvalidFileIdType() {
        // Test 1: file_id null (NOT NULL)
        AsyncTask testAsyncTask = creatAsyncTask(TaskStatus.QUEUED, null);
        testAsyncTask.setFile_id(null);
        testAsyncTask.setCompleted_at(null);
        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(testAsyncTask);
        });
    }
    @Test
    void shouldRejectInvalidStatusType() {
        // Test 2: status null (NOT NULL)
        AsyncTask testAsyncTask = creatAsyncTask(TaskStatus.COMPLETED, null);
        testAsyncTask.setStatus(null);
        testAsyncTask.setCompleted_at(LocalDateTime.now());
        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(testAsyncTask);
        });
    }

    @Test
    void shouldRejectInvalidCreatedAtType() {
        // Test 3: Validation Test for created_at null
        AsyncTask testAsyncTask = creatAsyncTask(TaskStatus.PROCESSING, null);
        testAsyncTask.setCreated_at(null);
        testAsyncTask.setCompleted_at(null);


        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<AsyncTask>> violations = validator.validate(testAsyncTask);
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
    void shouldNotNullIdCreatedAt() {
        AsyncTask testAsyncTask = creatAsyncTask(TaskStatus.COMPLETED, null);
        testAsyncTask.setCompleted_at(LocalDateTime.now());

        assertDoesNotThrow(() -> {
            AsyncTask saved = repository.saveAndFlush(testAsyncTask);
            assertNotNull(saved.getId());
            assertNotNull(saved.getCreated_at()); // Verifica @PrePersist
        });
    }

    private AsyncTask creatAsyncTask(TaskStatus status, String error_message) {
        AsyncTask task = new AsyncTask();
        task.setFile_id(UUID.fromString("27d0e7dd-93d5-4ce2-8212-16b3fff35163"));
        task.setStatus(status);
        task.setError_message(error_message);
        return task;
    }

}
