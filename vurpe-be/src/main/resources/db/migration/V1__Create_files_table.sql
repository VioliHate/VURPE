CREATE TABLE files (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       original_filename VARCHAR(100),
                       file_size BIGINT,
                       upload_status VARCHAR(50),
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_files_upload_status ON files(upload_status);
CREATE INDEX idx_files_created_at ON files(created_at);

