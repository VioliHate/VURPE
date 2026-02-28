package it.study.bruvio.vurpe.service;

import it.study.bruvio.vurpe.entity.BusinessRule;
import it.study.bruvio.vurpe.entity.DataRecord;
import it.study.bruvio.vurpe.repository.BusinessRuleRepository;
import it.study.bruvio.vurpe.repository.DataRecordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IntelligenceService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BusinessRuleRepository businessRuleRepository;
    private final DataRecordRepository dataRecordRepository;

    // SELF-INJECTION: Ci serve per far attivare il Proxy di Spring
    // Usiamo @Lazy per evitare riferimenti circolari all'avvio
    @Autowired
    @Lazy
    private IntelligenceService self;

    @Transactional
    public void applyBusinessRulesToFile(UUID fileId) {
        List<BusinessRule> allRules = businessRuleRepository.findAll();
        // Ordina per severity descrescente (o come preferisci)
        allRules.sort((r1, r2) -> Integer.compare(r2.getSeverity(), r1.getSeverity()));

        for (BusinessRule rule : allRules) {
            try {
                // CAMBIAMENTO FONDAMENTALE: Chiamo il metodo tramite 'self', non 'this'
                // Questo attiva REQUIRES_NEW creando una transazione separata per ogni regola
                self.applyRule(fileId, rule);
            } catch (Exception e) {
                // Ora l'errore su una regola non blocca le altre perché la transazione padre è salva
                System.err.println("Errore applicando regola ID " + rule.getId() + ": " + e.getMessage());
            }
        }
        this.setNoMatches(fileId);
    }

    // REQUIRES_NEW ora funzionerà perché chiamato tramite proxy
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void applyRule(UUID fileId, BusinessRule rule) {

        this.setRiskFlag(fileId, rule);
    }


    private void setRiskFlag(UUID fileId, BusinessRule rule) {

        // FIX SINTASSI: Sostituisco le virgolette doppie con singole per Postgres
        // (L'ideale sarebbe correggerle nel DB, ma questo protegge il codice)
        String safeCondition = rule.getRule_condition().replace("\"", "'");

        String sql = "SELECT * FROM data_records " +
                "WHERE file_id = :fileId " +
                "AND (" + safeCondition + ")";

        Query query = entityManager.createNativeQuery(sql, DataRecord.class);
        query.setParameter("fileId", fileId);

        @SuppressWarnings("unchecked")
        List<DataRecord> matchingRecords = query.getResultList();

        for (DataRecord record : matchingRecords) {
            // Logica di sovrascrittura flag
            if (record.getRisk_flag() == null || record.getRisk_flag().isEmpty()) {
                record.setRisk_flag(rule.getRisk_flag());
            }
        }

        if (!matchingRecords.isEmpty()) {
            dataRecordRepository.saveAll(matchingRecords);
        }
    }


    private void setNoMatches(UUID fileId){
            String takeNoMatchesSQL = "SELECT * FROM data_records " +
                    "WHERE file_id = :fileId " +
                    "AND risk_flag is NULL";
            Query takeNoMatchesQuery = entityManager.createNativeQuery(takeNoMatchesSQL, DataRecord.class);
            takeNoMatchesQuery.setParameter("fileId", fileId);

            @SuppressWarnings("unchecked")
            List<DataRecord> notMatchingRecords = takeNoMatchesQuery.getResultList();
            for(DataRecord record : notMatchingRecords) {
                record.setRisk_flag("NO_MATCHES");
            }
            if (!notMatchingRecords.isEmpty()) {
                dataRecordRepository.saveAll(notMatchingRecords);
            }
        }

}