CREATE TABLE async_tasks (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             file_id UUID NOT NULL,
                             status VARCHAR(50) NOT NULL,
                             error_message TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             completed_at TIMESTAMP,
                             FOREIGN KEY (file_id) REFERENCES files(id)
);


CREATE INDEX idx_async_tasks_file_id ON async_tasks(file_id);
CREATE INDEX idx_async_tasks_status ON async_tasks(status);