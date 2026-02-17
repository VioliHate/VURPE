package it.study.bruvio.vurpe.entity;

import jakarta.persistence.*;
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
public class AnalyisiResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "record_count")
    private Integer recordCount;

    @Column(name = "average_amount")
    private BigDecimal averageAmount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "distribution_by_category", columnDefinition = "jsonb")
    private Map<String, Integer> distributionByCategory;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "distribution_by_risk_flag", columnDefinition = "jsonb")
    private Map<String, Integer> distributionByRiskFlag;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "time_series_by_date", columnDefinition = "jsonb")
    private Map<String, BigDecimal> timeSeriesByDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
