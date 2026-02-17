CREATE TABLE analysis_results (
                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                  file_id UUID NOT NULL,
                                  total_amount DECIMAL(15, 2),
                                  record_count INTEGER,
                                  average_amount DECIMAL(15, 2),
                                  distribution_by_category JSONB,
                                  distribution_by_risk_flag JSONB,
                                  time_series_by_date JSONB,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (file_id) REFERENCES files(id)
);


CREATE INDEX idx_analysis_results_file_id ON analysis_results(file_id);
