package it.study.bruvio.vurpe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "analysis_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "file_id", nullable = false)
    private UUID file_id;

    @Column(name = "total_amount")
    private BigDecimal total_amount;

    @Column(name = "record_count")
    private Integer record_count;

    @Column(name = "average_amount")
    private BigDecimal average_amount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "distribution_by_category", columnDefinition = "jsonb")
    private Map<String, Integer> distribution_by_category;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "distribution_by_risk_flag", columnDefinition = "jsonb")
    private Map<String, Integer> distribution_by_risk_flag;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "time_series_by_date", columnDefinition = "jsonb")
    private Map<String, BigDecimal> time_series_by_date;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }
}
