CREATE TABLE data_records (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              file_id UUID NOT NULL,
                              original_id VARCHAR(255) NOT NULL,
                              amount DECIMAL(15, 2) NOT NULL,
                              category VARCHAR(100) NOT NULL,
                              date TIMESTAMP NOT NULL,
                              description TEXT,
                              risk_flag VARCHAR(50),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE
);

-- Indici
CREATE INDEX idx_data_records_file_id ON data_records(file_id);
CREATE INDEX idx_data_records_date ON data_records(date);
CREATE INDEX idx_data_records_category ON data_records(category);