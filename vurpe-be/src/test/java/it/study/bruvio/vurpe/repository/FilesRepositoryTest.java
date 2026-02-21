package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.Files;
import jakarta.persistence.PersistenceException;
import jakarta.validation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Set;

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
        // Test 1: Application Test for created_at null
        Files testFile = createTestFile("test.csv", 1024L, "UPLOADED");
        testFile.setCreated_at(null);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Files>> violations = validator.validate(testFile);
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
    void shouldPreventDuplicateId() {
        Files file1 = createTestFile("dashboard-data.csv", 2048L, "COMPLETED");
        entityManager.persistAndFlush(file1);

        Files file2 = createTestFile("dashboard-data-copy.csv", 4096L, "COMPLETED");
        file2.setId(file1.getId());

        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(file2);
        });
    }


    // metodo per creare al volo un file
    private Files createTestFile(String original_name, Long file_size, String upload_status) {
        Files file = new Files();
        file.setOriginal_name(original_name);
        file.setFile_size(file_size);
        file.setUpload_status(upload_status);
        return file;
    }
}
