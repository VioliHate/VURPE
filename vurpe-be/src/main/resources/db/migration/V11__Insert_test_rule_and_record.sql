-- V11__Insert_valid_test_rules_and_records.sql

-- Prima cancella le regole vecchie con condizioni invalide
DELETE FROM data_records;

-- Inserisci regole VALIDE con colonne che esistono in data_records
INSERT INTO business_rules (
    id,
    rule_name,
    rule_condition,
    risk_flag,
    severity
) VALUES
      ('11111111-1111-1111-1111-111111111111',
       'Importo molto elevato',
       'amount > 10000',
       'HIGH',
       9),

      ('22222222-2222-2222-2222-222222222222',
       'Importo elevato categoria transfer',
       'amount > 5000 AND category = ''transfer''',
       'MEDIUM',
       7),

      ('33333333-3333-3333-3333-333333333333',
       'Categoria internal',
       'category = ''internal''',
       'LOW',
       3),

      ('44444444-4444-4444-4444-444444444444',
       'Importo negativo (rimborso)',
       'amount < 0',
       'MEDIUM',
       5),

      ('55555555-5555-5555-5555-555555555555',
       'Descrizione vuota o nulla',
       'description IS NULL OR length(description) < 5',
       'LOW',
       4),

      ('66666666-6666-6666-6666-666666666666',
       'Categoria TRAVEL con importo alto',
       'category = ''TRAVEL'' AND amount > 3000',
       'MEDIUM',
       6),

      ('77777777-7777-7777-7777-777777777777',
       'Categoria OFFICE',
       'category = ''OFFICE''',
       'LOW',
       2),

      ('88888888-8888-8888-8888-888888888888',
       'Importo tra 1000 e 3000',
       'amount BETWEEN 1000 AND 3000',
       'LOW',
       3);

-- Inserisci record di test senza risk_flag
INSERT INTO data_records (
    id,
    file_id,
    original_id,
    amount,
    category,
    date,
    description,
    risk_flag,
    created_at,
    updated_at
) VALUES
      -- Record che matcha "Importo molto elevato" (amount > 10000)
      ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
       'a1491137-a39d-440a-8edf-5af3ddfe4558',
       'TXN-TEST-001',
       15000.00,
       'OFFICE',
       '2026-02-27 10:00:00',
       'Test importo molto elevato',
       NULL,
       '2026-02-27 10:00:00',
       '2026-02-27 10:00:00'),

      -- Record che matcha "Importo tra 1000 e 3000"
      ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
       'a1491137-a39d-440a-8edf-5af3ddfe4558',
       'TXN-TEST-002',
       2500.00,
       'SUPPLIES',
       '2026-02-27 11:00:00',
       'Test importo medio',
       NULL,
       '2026-02-27 11:00:00',
       '2026-02-27 11:00:00'),

      -- Record che matcha "Categoria TRAVEL con importo alto"
      ('cccccccc-cccc-cccc-cccc-cccccccccccc',
       'a1491137-a39d-440a-8edf-5af3ddfe4558',
       'TXN-TEST-003',
       3500.00,
       'TRAVEL',
       '2026-02-27 12:00:00',
       'Test viaggio costoso',
       NULL,
       '2026-02-27 12:00:00',
       '2026-02-27 12:00:00'),

      -- Record che matcha "Importo negativo"
      ('dddddddd-dddd-dddd-dddd-dddddddddddd',
       'a1491137-a39d-440a-8edf-5af3ddfe4558',
       'TXN-TEST-004',
       -500.00,
       'OTHER',
       '2026-02-27 13:00:00',
       'Test rimborso',
       NULL,
       '2026-02-27 13:00:00',
       '2026-02-27 13:00:00'),

      -- Record che matcha "Descrizione vuota"
      ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
       'a1491137-a39d-440a-8edf-5af3ddfe4558',
       'TXN-TEST-005',
       800.00,
       'IT',
       '2026-02-27 14:00:00',
       'Test',
       NULL,
       '2026-02-27 14:00:00',
       '2026-02-27 14:00:00');
