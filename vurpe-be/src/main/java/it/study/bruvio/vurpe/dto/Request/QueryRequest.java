package it.study.bruvio.vurpe.dto.Request;



import lombok.Data;
import java.util.List;

@Data
public class QueryRequest {
    // Campo su cui fare calcoli (es. "amount")
    private String field;

    // Operazione: "SUM", "COUNT", "AVG", "MIN", "MAX"
    private String operation;

    // Campo per raggruppamento (es. "category", "riskFlag", "date")
    // Se null, restituisce un singolo valore totale.
    private String groupBy;

    // Lista di filtri (WHERE)
    private List<Filter> filters;

    // Limite risultati (es. Top 10)
    private Integer limit;

    @Data
    public static class Filter {
        private String field;    // es. "riskFlag"
        private String operator; // "=", ">", "<", "LIKE", ">="
        private String value;    // es. "high_risk"
    }
}