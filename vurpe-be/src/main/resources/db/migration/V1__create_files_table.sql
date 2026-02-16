CREATE SEQUENCE IF NOT EXISTS files_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE files
(
    id                BIGINT NOT NULL,
    original_filename VARCHAR(100),
    file_size         BIGINT,
    upload_status     VARCHAR(255),
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_files PRIMARY KEY (id)
);