package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.Files;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Test FILES repo")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FilesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FilesRepository repository;

    @Test
    @DisplayName("All records must have non-null IDs and create_at")
    void everyRecordMustHaveNonNullIdAndCreatedAt() {
        // take all record
        List<Files> allRecords = repository.findAll();

        assertThat(allRecords)
                .as("There must be at least some test records")
                .isNotEmpty();

        allRecords.forEach(record -> {
            assertThat(record.getId())
                    .as("ID must be not null for record: " + record)
                    .isNotNull();

            assertThat(record.getCreated_at())
                    .as("created_at must be not null for record: " + record)
                    .isNotNull();
        });
    }
    @Test
    void shouldRejectInvalidDataTypes() {
        // Test 1: created_at null (NOT NULL)
        assertThrows(Exception.class, () -> {
            Files file = new Files();
            file.setOriginal_name("test.csv");
            file.setFile_size(1024L);
            file.setUpload_status("UPLOADED");
            file.setCreated_at(null); // INVALIDO
            repository.saveAndFlush(file);
        });
    }

    @Test
    void shouldAcceptValidDataTypes() {
        assertDoesNotThrow(() -> {
            Files file = new Files();
            file.setOriginal_name("dashboard-data.csv");
            file.setFile_size(2048L);
            file.setUpload_status("COMPLETED");
            file.setCreated_at(Instant.now());

            Files saved = repository.saveAndFlush(file);
            assertNotNull(saved.getId());
            assertEquals("dashboard-data.csv", saved.getOriginal_name());
            assertEquals(2048L, saved.getFile_size());
        });
    }

    @Test
    void shouldAcceptNullableFields() {
        assertDoesNotThrow(() -> {
            Files file = new Files();
            file.setOriginal_name(null); // Nullable
            file.setFile_size(null); // Nullable
            file.setUpload_status(null); // Nullable
            file.setCreated_at(Instant.now()); // NOT NULL

            Files saved = repository.saveAndFlush(file);
            assertNotNull(saved.getId());
            assertNull(saved.getOriginal_name());
            assertNull(saved.getFile_size());
            assertNull(saved.getUpload_status());
        });
    }
}
