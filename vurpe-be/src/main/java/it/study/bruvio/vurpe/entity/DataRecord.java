package it.study.bruvio.vurpe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "data_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "file_id", nullable = false)
    private UUID fileId;

    @Column(name = "original_id", nullable = false)
    private String originalId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "description")
    private String description;

    @Column(name = "risk_flag")
    private String riskFlag;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DataRecord(UUID idFIle, String id, BigDecimal bigDecimal, String s1, LocalDateTime parse, String s2) {
        this.setFileId(idFIle);
        this.setOriginalId(id);
        this.setAmount(bigDecimal);
        this.setCategory(s1);
        this.setDate(parse);
        this.setDescription(s2);

    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
