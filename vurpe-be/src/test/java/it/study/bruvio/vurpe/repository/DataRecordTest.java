package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.DataRecord;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DataJpaTest
@DisplayName("Test FILES repo")
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
        // Tenta di inserire un amount non numerico
        DataRecord record = new DataRecord();

        record.setFile_id(UUID.randomUUID());
        record.setOriginal_id("TEST-001");
        // amount è BigDecimal, quindi questo compila ma potrebbe fallire
        record.setAmount(null); // NULL non permesso se NOT NULL
        record.setCategory("Test");
        record.setDate(LocalDateTime.now());

        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(record);
        });
    }

    @Test
    void shouldRejectInvalidDateType() {
        DataRecord record = new DataRecord();
        record.setFile_id(UUID.randomUUID());
        record.setOriginal_id("TEST-002");
        record.setAmount(new BigDecimal("100.00"));
        record.setCategory("Test");
        record.setDate(null); // NULL non permesso

        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(record);
        });
    }

    @Test
    void shouldRejectInvalidUUIDType() {
        DataRecord record = new DataRecord();
        record.setFile_id(null); // UUID NULL non permesso
        record.setOriginal_id("TEST-003");
        record.setAmount(new BigDecimal("100.00"));
        record.setCategory("Test");
        record.setDate(LocalDateTime.now());

        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(record);
        });
    }

    @Test
    void shouldAcceptValidDataTypes() {
        DataRecord record = new DataRecord();
        record.setFile_id(UUID.randomUUID());
        record.setOriginal_id("TEST-004");
        record.setAmount(new BigDecimal("100.00"));
        record.setCategory("Test");
        record.setDate(LocalDateTime.now());

        assertDoesNotThrow(() -> {
            DataRecord saved = repository.saveAndFlush(record);
            assertNotNull(saved.getId());
        });
    }
}
