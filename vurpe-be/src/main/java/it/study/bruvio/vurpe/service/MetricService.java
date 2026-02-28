package it.study.bruvio.vurpe.service;




import it.study.bruvio.vurpe.dto.Request.QueryRequest;
import it.study.bruvio.vurpe.dto.response.QueryResult;
import it.study.bruvio.vurpe.entity.DataRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricService {

    @PersistenceContext
    private EntityManager entityManager;

    public QueryResult executeQuery(QueryRequest req) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        Map<String, Object> resultMap;

        // Decisione: È una query di aggregazione (GROUP BY) o scalare (Totale)?
        if (req.getGroupBy() != null && !req.getGroupBy().isBlank()) {
            resultMap = executeGroupByQuery(cb, req);
        } else {
            resultMap = executeScalarQuery(cb, req);
        }

        String chartType = suggestChartType(req);
        return new QueryResult(req.getOperation(), req.getGroupBy(), resultMap, chartType);
    }

    // --- Scenario 1: Group By (es. Somma per Categoria) ---
    private Map<String, Object> executeGroupByQuery(CriteriaBuilder cb, QueryRequest req) {
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<DataRecord> root = query.from(DataRecord.class);

        // 1. Definisci la colonna di raggruppamento
        // Nota: Qui assumiamo che il nome del campo nel JSON corrisponda al campo Java Entity
        Expression<?> groupPath = root.get(req.getGroupBy());

        // 2. Definisci l'operazione (SELECT category, SUM(amount))
        Expression<? extends Number> operationPath = buildOperation(cb, root, req);

        query.multiselect(groupPath, operationPath);

        // 3. Applica Filtri (WHERE)
        applyFilters(cb, query, root, req.getFilters());

        // 4. Applica Group By
        query.groupBy(groupPath);

        // 5. Opzionale: Ordina per valore decrescente
        query.orderBy(cb.desc(operationPath));

        // Esecuzione
        int limit = (req.getLimit() != null) ? req.getLimit() : 1000;
        List<Object[]> results = entityManager.createQuery(query)
                .setMaxResults(limit)
                .getResultList();

        // Trasformazione in Map
        Map<String, Object> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            String key = String.valueOf(row[0]);
            Object val = row[1];
            map.put(key, val);
        }
        return map;
    }

    // --- Scenario 2: Scalare (es. Totale complessivo) ---
    private Map<String, Object> executeScalarQuery(CriteriaBuilder cb, QueryRequest req) {
        CriteriaQuery<Number> query = cb.createQuery(Number.class);
        Root<DataRecord> root = query.from(DataRecord.class);

        // 1. Selezione
        query.select(buildOperation(cb, root, req));

        // 2. Filtri
        applyFilters(cb, query, root, req.getFilters());

        // Esecuzione
        Number result = entityManager.createQuery(query).getSingleResult();
        return Map.of("total", result != null ? result : 0);
    }

    // --- Helper: Costruzione Operazione ---
    private Expression<? extends Number> buildOperation(CriteriaBuilder cb, Root<DataRecord> root, QueryRequest req) {
        String field = req.getField(); // es. "amount"

        // Se l'operazione è COUNT, non serve un campo numerico specifico
        if ("COUNT".equalsIgnoreCase(req.getOperation())) {
            return cb.count(root);
        }

        // Per operazioni matematiche, serve il campo (es. amount)
        if (field == null) throw new IllegalArgumentException("Field is required for " + req.getOperation());

        return switch (req.getOperation().toUpperCase()) {
            case "SUM" -> cb.sum(root.get(field));
            case "AVG" -> cb.avg(root.get(field));
            case "MIN" -> cb.min(root.get(field));
            case "MAX" -> cb.max(root.get(field));
            default -> throw new IllegalArgumentException("Unknown operation: " + req.getOperation());
        };
    }

    // --- Helper: Applicazione Filtri ---
    private void applyFilters(CriteriaBuilder cb, CriteriaQuery<?> query, Root<DataRecord> root, List<QueryRequest.Filter> filters) {
        if (filters == null || filters.isEmpty()) return;

        List<Predicate> predicates = new ArrayList<>();
        for (QueryRequest.Filter f : filters) {
            String fieldName = f.getField();
            String op = f.getOperator();
            String val = f.getValue();

            // Nota: Qui serve gestire i tipi. JPA vuole tipi corretti (BigDecimal vs String vs Date)
            // Per semplicità facciamo un casting dinamico basilare
            Path<Comparable> path = root.get(fieldName);
            Comparable typedValue = castToType(root.get(fieldName).getJavaType(), val);

            switch (op) {
                case "=" -> predicates.add(cb.equal(path, typedValue));
                case ">" -> predicates.add(cb.greaterThan(path, typedValue));
                case "<" -> predicates.add(cb.lessThan(path, typedValue));
                case ">=" -> predicates.add(cb.greaterThanOrEqualTo(path, typedValue));
                case "<=" -> predicates.add(cb.lessThanOrEqualTo(path, typedValue));
                case "LIKE" -> predicates.add(cb.like(root.get(fieldName).as(String.class), "%" + val + "%"));
            }
        }
        query.where(predicates.toArray(new Predicate[0]));
    }

    // --- Helper: Conversione Tipi (Cruciale per filtri funzionanti) ---
    private Comparable castToType(Class<?> type, String value) {
        try {
            if (type.equals(BigDecimal.class)) return new BigDecimal(value);
            if (type.equals(Integer.class)) return Integer.parseInt(value);
            if (type.equals(Long.class)) return Long.parseLong(value);
            if (type.equals(Double.class)) return Double.parseDouble(value);
            if (type.equals(LocalDateTime.class)) return LocalDateTime.parse(value); // Formato ISO-8601
            if (type.equals(LocalDate.class)) return LocalDate.parse(value);
            if (type.equals(UUID.class)) return UUID.fromString(value);
            return value; // Default String
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot cast value " + value + " to type " + type.getName());
        }
    }

    private String suggestChartType(QueryRequest req) {
        if (req.getGroupBy() == null) return "card";
        if (req.getGroupBy().equalsIgnoreCase("date")) return "line";
        if (req.getGroupBy().equalsIgnoreCase("category")) return "pie"; // o bar
        return "bar";
    }
}