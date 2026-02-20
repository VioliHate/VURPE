package it.study.bruvio.vurpe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(max = 100)
    @Column(name = "original_filename")
    private String original_name;

    @Column(name = "file_size")
    private Long file_size;

    @Column (name = "upload_status")
    private String upload_status;

    @NotNull
    @Column (name= "created_at",nullable = false)
    private Instant created_at;

    @PrePersist
    protected void onCreate() {
        created_at = Instant.now();
    }

}
