CREATE TABLE data_records
(
    id          UUID         NOT NULL,
    file_id     UUID         NOT NULL,
    original_id VARCHAR(255) NOT NULL,
    amount      DECIMAL      NOT NULL,
    category    VARCHAR(255) NOT NULL,
    date        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description VARCHAR(255),
    risk_flag   VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_data_records PRIMARY KEY (id)
);
