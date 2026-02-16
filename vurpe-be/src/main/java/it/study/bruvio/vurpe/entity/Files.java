package it.study.bruvio.vurpe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "files")
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_id_gen")
    @SequenceGenerator(name = "files_id_gen", sequenceName = "files_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "original_filename")
    private String name;

    @Column(name = "file_size")
    private Long size;

    @Column (name = "upload_status")
    private String status;

    @NotNull
    @Column (name= "created_at",nullable = false)
    private Instant createdAt;

}
