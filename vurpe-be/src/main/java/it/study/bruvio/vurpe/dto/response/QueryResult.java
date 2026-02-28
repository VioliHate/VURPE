package it.study.bruvio.vurpe.dto.response;



import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class QueryResult {
    private String operation;       // L'operazione richiesta
    private String groupBy;         // Il campo di raggruppamento usato
    private Map<String, Object> data; // I risultati (Key -> Value)
    private String suggestedChart;  // Suggerimento per il FE (Bar, Pie, Line)
}