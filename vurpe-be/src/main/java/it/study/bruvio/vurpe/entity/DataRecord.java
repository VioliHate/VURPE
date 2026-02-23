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


    @Column(name="file_id",nullable = false)
    private UUID file_id;

    @Column(name = "original_id", nullable = false)
    private String original_id;


    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "description")
    private String description;

    @Column(name = "risk_flag")
    private String risk_flag;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    public DataRecord(UUID idFIle, String id, BigDecimal bigDecimal, String s1, LocalDateTime parse, String s2) {
        this.setFile_id(idFIle);
        this.setOriginal_id(id);
        this.setAmount(bigDecimal);
        this.setCategory(s1);
        this.setDate(parse);
        this.setDescription(s2);

    }


    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }
}
