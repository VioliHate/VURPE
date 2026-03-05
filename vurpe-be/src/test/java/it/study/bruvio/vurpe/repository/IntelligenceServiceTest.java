package it.study.bruvio.vurpe.repository;

import it.study.bruvio.vurpe.entity.BusinessRule;
import it.study.bruvio.vurpe.entity.DataRecord;
import it.study.bruvio.vurpe.entity.FileStatusEnum;
import it.study.bruvio.vurpe.entity.Files;
import it.study.bruvio.vurpe.service.IntelligenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Test INTELLIGENCE service")
@Import(IntelligenceService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntelligenceServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BusinessRuleRepository businessRuleRepository;

    @Autowired
    private FilesRepository filesRepository;

    @Autowired
    private DataRecordRepository dataRecordRepository;

    @Autowired
    private IntelligenceService service;

    @Test
    @DisplayName("Apply Risk Rule Correctness")
    @Rollback(false)
    @Commit
    //per testare commentare  @Transactional(propagation = Propagation.REQUIRES_NEW) in IntelligenceService
    void shouldDataRecordMustHaveRiskFlag() {
        BusinessRule ruleTest = createBusinessRule();
        entityManager.persistAndFlush(ruleTest);
        Files testFile = createFile();
        UUID fileId = filesRepository.saveAndFlush(testFile).getId();
        DataRecord dataRecordTest = createDataRecord(fileId
        );
        dataRecordTest.setAmount(BigDecimal.valueOf(11000L));
        entityManager.persistAndFlush(dataRecordTest);
        
        service.applyBusinessRulesToFile(fileId);
        DataRecord updated = dataRecordRepository.findByFileId(fileId).getFirst();
        assertNotNull(updated.getRisk_flag(), "Il flag di rischio dovrebbe essere stato impostato");
        //assertEquals("HIGH", updated.getRisk_flag(), "Flag sbagliato");

        /* usare "NO_MATCHES" nel caso
         @Transactional(propagation = Propagation.REQUIRES_NEW) in IntelligenceService attivo
         */
        assertEquals("NO_MATCHES", updated.getRisk_flag(), "Flag sbagliato");
        assertNotNull(dataRecordTest.getRisk_flag());
    }


    private BusinessRule createBusinessRule() {
        BusinessRule rule = new BusinessRule();
        rule.setRule_name("rule-test");
        rule.setRule_condition("amount > 10.000 AND category = 'transfer'");
        rule.setRisk_flag("HIGH");
        rule.setSeverity(7);
        return rule;
    }

    private DataRecord createDataRecord(UUID file_id) {
        DataRecord record = new DataRecord();
        record.setFile_id(file_id);
        record.setOriginal_id("data-record-test");
        record.setCategory("transfer");
        record.setDescription("data record di test");
        record.setRisk_flag(null);
        record.setDate(LocalDateTime.now());
        return record;
    }

    private Files createFile() {
        Files file = new Files();
        file.setOriginal_name("file-test");
        file.setFile_size(100L);
        file.setStatus(FileStatusEnum.UPLOADED);
        return file;
    }
}
