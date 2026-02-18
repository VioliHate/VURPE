package it.study.bruvio.vurpe.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "async_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "file_id", nullable = false)
    private UUID file_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Column(name = "error_message")
    private String error_message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "completed_at")
    private LocalDateTime completed_at;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }
}
