package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.DataRecord;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Test DATA RECORD repo")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DataRecordTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DataRecordRepository repository;

    @Test
    @DisplayName("All records must have non-null IDs and create_at")
    void everyRecordMustHaveNonNullIdAndCreatedAt() {
        // take all record
        List<DataRecord> allRecords = repository.findAll();

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
    void shouldRejectInvalidAmountType() {
        // Amount not null
        DataRecord record = createDataRecord("27d0e7dd-93d5-4ce2-8212-16b3fff35163", "TEST-001",
                "TEST","data record di test", "HIGHT");
        record.setDate(LocalDateTime.now());
        record.setAmount(null);
        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(record);
        });
    }

    @Test
    void shouldRejectInvalidDateType() {
        // Date not null
        DataRecord record = createDataRecord("27d0e7dd-93d5-4ce2-8212-16b3fff35163", "TEST-002",
                "TEST","data record di test", "HIGHT");
        record.setDate(null);
        record.setAmount(BigDecimal.valueOf(1000L));
        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(record);
        });
    }

    @Test
    void shouldRejectInvalidUUIDType() {
        // UUID not null
        DataRecord record = createDataRecord("27d0e7dd-93d5-4ce2-8212-16b3fff35163", "TEST-003",
                "TEST","data record di test", "HIGHT");
        record.setFile_id(null);
        record.setDate(LocalDateTime.now());
        record.setAmount(BigDecimal.valueOf(1000L));
        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(record);
        });
    }

    @Test
    void shouldRejectInvalidCategoryType() {
        DataRecord record = createDataRecord("27d0e7dd-93d5-4ce2-8212-16b3fff35163", "TEST-004",
                "TEST","data record di test", "HIGHT");
        record.setCategory(null);
        record.setDate(LocalDateTime.now());
        record.setAmount(BigDecimal.valueOf(1000L));
        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(record);
        });
    }
    @Test
    void shouldRejectInvalidOriginalIdType() {
        DataRecord record = createDataRecord("27d0e7dd-93d5-4ce2-8212-16b3fff35163", "TEST-005",
                "TEST","data record di test", "HIGHT");
        record.setOriginal_id(null);
        record.setDate(LocalDateTime.now());
        record.setAmount(BigDecimal.valueOf(1000L));
        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(record);
        });
    }

    @Test
    void shouldAcceptValidDataTypes() {
        // check insert
        DataRecord record = createDataRecord("27d0e7dd-93d5-4ce2-8212-16b3fff35163", "TEST-006",
                "TEST","data record di test", "HIGHT");
        record.setAmount(new BigDecimal("100.00"));
        record.setCategory("Test");
        record.setDate(LocalDateTime.now());

        assertDoesNotThrow(() -> {
            DataRecord saved = repository.saveAndFlush(record);
            assertNotNull(saved.getId());
        });
    }

    //metodo creazione DataRecord di test
    private DataRecord createDataRecord(String file_id, String original_id,
                                        String category,
                                        String description, String risk_flag) {
        DataRecord record = new DataRecord();
        record.setFile_id(UUID.fromString(file_id));
        record.setOriginal_id(original_id);
        record.setCategory(category);
        record.setDescription(description);
        record.setRisk_flag(risk_flag);
        return record;
    }
}
