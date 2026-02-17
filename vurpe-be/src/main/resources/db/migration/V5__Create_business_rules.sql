CREATE TABLE business_rules (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                rule_name VARCHAR(255) NOT NULL,
                                rule_condition VARCHAR(500) NOT NULL,
                                risk_flag VARCHAR(50) NOT NULL,
                                severity INTEGER NOT NULL
);

-- Indici per migliorare le query
CREATE INDEX idx_business_rules_risk_flag ON business_rules(risk_flag);
CREATE INDEX idx_business_rules_severity ON business_rules(severity);
CREATE INDEX idx_business_rules_rule_name ON business_rules(rule_name);
