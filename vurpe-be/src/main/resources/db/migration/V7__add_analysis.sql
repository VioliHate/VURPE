INSERT INTO analysis_results (
    id,
    file_id,
    total_amount,
    record_count,
    average_amount,
    distribution_by_category,
    distribution_by_risk_flag,
    time_series_by_date,
    created_at
) VALUES (
             gen_random_uuid(),
             '11111111-1111-1111-1111-111111111111',
             15000.50,
             120,
             125.00,
             '{"SUPPLIES": 40, "TRAVEL": 30, "OTHER": 50}'::jsonb,
             '{"LOW": 90, "MEDIUM": 20, "HIGH": 10}'::jsonb,
             '{"2026-02-15": 5000.00, "2026-02-16": 7000.50, "2026-02-17": 3000.00}'::jsonb,
             CURRENT_TIMESTAMP
         );