# Requirements Document: Smart-BPM Dashboard

## Introduction

Smart-BPM Dashboard è un'applicazione full-stack enterprise per l'analisi di dati aziendali attraverso un approccio ibrido ML + Rules Engine. L'applicazione consente agli utenti di caricare file CSV, applicare regole di business e visualizzare insight attraverso una dashboard interattiva. Questo progetto dimostra competenze in architetture Enterprise AI-driven, seguendo i principi di Clean Architecture e best practices di sviluppo.

## Goals

- Provide a robust platform for uploading and analyzing business data through CSV files
- Apply configurable business rules to automatically identify risks and generate insights
- Deliver an interactive, responsive dashboard for data exploration and visualization
- Ensure reliable data persistence and asynchronous processing for scalability
- Enable seamless deployment through Docker containerization with well-defined APIs

## Glossary

- **System**: Smart-BPM Dashboard - l'applicazione completa
- **CSV_File**: File in formato Comma-Separated Values contenente dati aziendali
- **Data_Record**: Singola riga di dati caricata dal CSV
- **Business_Rule**: Regola IF-THEN che determina insight e flag sui dati
- **Insight**: Risultato dell'applicazione di regole di business ai dati
- **Dashboard**: Interfaccia web per visualizzare dati e insight
- **Ingestion_Service**: Servizio backend responsabile del caricamento e validazione file
- **Intelligence_Service**: Servizio backend responsabile dell'applicazione di regole e calcolo metriche
- **Visualization_Layer**: Componenti frontend per la presentazione dei dati
- **PostgreSQL_Database**: Database relazionale per la persistenza dei dati
- **Risk_Flag**: Indicatore di rischio assegnato ai dati in base alle regole di business
- **Async_Task**: Elaborazione asincrona di analisi su dataset

## Requirements

### Requirement 1: CSV File Upload and Validation

**User Story:** As a business analyst, I want to upload CSV files to the system, so that I can load business data for analysis.

#### Acceptance Criteria

1. WHEN a user submits a CSV file through the upload endpoint THEN the Ingestion_Service SHALL validate the file format and return success or error response
2. WHEN a CSV file is submitted THEN the Ingestion_Service SHALL verify the file size does not exceed 50MB
3. WHEN a CSV file is submitted THEN the Ingestion_Service SHALL validate that the file contains required columns (id, amount, category, date, description)
4. WHEN a CSV file contains invalid data THEN the Ingestion_Service SHALL return a detailed error message indicating which rows failed validation
5. WHEN a CSV file is valid THEN the Ingestion_Service SHALL parse the file and store all Data_Records in PostgreSQL_Database
6. WHEN a Data_Record is stored THEN the Ingestion_Service SHALL assign a unique identifier and timestamp to each record

### Requirement 2: Data Persistence and Schema Management

**User Story:** As a system administrator, I want data to be reliably stored in PostgreSQL, so that I can ensure data integrity and enable future analysis.

#### Acceptance Criteria

1. THE System SHALL create and maintain a PostgreSQL_Database schema with tables for Data_Records and Analysis_Results
2. WHEN the System starts THEN database migrations SHALL automatically execute to ensure schema consistency
3. WHEN a Data_Record is inserted THEN the System SHALL enforce data type constraints (amount as decimal, date as timestamp, category as string)
4. WHEN a Data_Record is stored THEN the System SHALL maintain referential integrity and prevent orphaned records
5. WHEN data is queried THEN the System SHALL return results within 500ms for datasets up to 100,000 records

### Requirement 3: Business Rules Engine

**User Story:** As a business analyst, I want to apply business rules to data, so that I can automatically identify risks and generate insights.

#### Acceptance Criteria

1. WHEN Data_Records are loaded THEN the Intelligence_Service SHALL apply all configured Business_Rules to each record
2. WHEN a Business_Rule condition is met THEN the Intelligence_Service SHALL assign the corresponding Risk_Flag to the Data_Record
3. WHEN a rule evaluates "IF amount > 10000 AND category = 'transfer' THEN Risk_Flag = 'high_risk'" THEN the System SHALL correctly identify and flag matching records
4. WHEN a rule evaluates "IF amount > 5000 AND amount <= 10000 AND category = 'transfer' THEN Risk_Flag = 'medium_risk'" THEN the System SHALL correctly identify and flag matching records
5. WHEN a rule evaluates "IF category = 'internal' THEN Risk_Flag = 'low_risk'" THEN the System SHALL correctly identify and flag matching records
6. WHEN Business_Rules are applied THEN the Intelligence_Service SHALL execute the analysis asynchronously and notify the user upon completion
7. WHEN multiple Business_Rules apply to a single record THEN the System SHALL assign the highest severity Risk_Flag

### Requirement 4: Metrics and Insights Calculation

**User Story:** As a business analyst, I want to see calculated metrics and insights, so that I can understand data patterns and trends.

#### Acceptance Criteria

1. WHEN Data_Records are analyzed THEN the Intelligence_Service SHALL calculate aggregate metrics including total amount, record count, and average amount
2. WHEN Data_Records are analyzed THEN the Intelligence_Service SHALL calculate distribution metrics by category and Risk_Flag
3. WHEN Data_Records are analyzed THEN the Intelligence_Service SHALL calculate time-series metrics aggregated by date
4. WHEN metrics are calculated THEN the System SHALL store results in PostgreSQL_Database for retrieval
5. WHEN metrics are requested THEN the System SHALL return results within 200ms

### Requirement 5: Interactive Data Visualization

**User Story:** As a business analyst, I want to view data in interactive tables and charts, so that I can explore insights visually.

#### Acceptance Criteria

1. WHEN the Dashboard loads THEN the Visualization_Layer SHALL display a paginated table of all Data_Records with columns: id, amount, category, date, description, Risk_Flag
2. WHEN a user interacts with the table THEN the Visualization_Layer SHALL support sorting by any column
3. WHEN a user interacts with the table THEN the Visualization_Layer SHALL support filtering by category and Risk_Flag
4. WHEN a user searches THEN the Visualization_Layer SHALL filter records by description text in real-time
5. WHEN the Dashboard loads THEN the Visualization_Layer SHALL display a bar chart showing record count by Risk_Flag
6. WHEN the Dashboard loads THEN the Visualization_Layer SHALL display a line chart showing total amount by date
7. WHEN the Dashboard loads THEN the Visualization_Layer SHALL display a pie chart showing distribution by category
8. WHEN a user applies filters THEN all charts SHALL update to reflect the filtered dataset

### Requirement 6: Responsive User Interface

**User Story:** As a user, I want the dashboard to work on different screen sizes, so that I can access it from any device.

#### Acceptance Criteria

1. WHEN the Dashboard is viewed on a desktop screen THEN the Visualization_Layer SHALL display all components in a multi-column layout
2. WHEN the Dashboard is viewed on a tablet screen THEN the Visualization_Layer SHALL display components in a responsive two-column layout
3. WHEN the Dashboard is viewed on a mobile screen THEN the Visualization_Layer SHALL display components in a single-column layout
4. WHEN the Dashboard is resized THEN all charts and tables SHALL reflow without breaking layout
5. WHEN a user interacts with the Dashboard on mobile THEN touch interactions SHALL work correctly for sorting, filtering, and scrolling

### Requirement 7: Error Handling and User Feedback

**User Story:** As a user, I want clear error messages and feedback, so that I can understand what went wrong and how to fix it.

#### Acceptance Criteria

1. WHEN a file upload fails THEN the System SHALL display a user-friendly error message indicating the reason (invalid format, file too large, missing columns)
2. WHEN data validation fails THEN the System SHALL provide specific row numbers and field names that caused the failure
3. WHEN an analysis fails THEN the System SHALL display an error notification and log the error for debugging
4. WHEN an operation succeeds THEN the System SHALL display a success notification to the user
5. WHEN the user performs an action THEN the System SHALL provide visual feedback (loading indicators, progress bars) for long-running operations

### Requirement 8: Asynchronous Analysis Processing

**User Story:** As a system administrator, I want analysis to run asynchronously, so that the UI remains responsive during long-running operations.

#### Acceptance Criteria

1. WHEN Data_Records are loaded THEN the Intelligence_Service SHALL queue an Async_Task for analysis
2. WHEN an Async_Task is queued THEN the System SHALL return immediately to the user with a task ID
3. WHEN an Async_Task completes THEN the System SHALL update the UI with results via WebSocket or polling
4. WHEN an Async_Task fails THEN the System SHALL store error information and notify the user
5. WHEN multiple Async_Tasks are queued THEN the System SHALL process them sequentially to prevent resource exhaustion

### Requirement 9: Docker Containerization and Deployment

**User Story:** As a DevOps engineer, I want the application to run in Docker containers, so that I can deploy it consistently across environments.

#### Acceptance Criteria

1. WHEN Docker Compose is executed THEN the System SHALL start all required services: Spring Boot backend, PostgreSQL database, and Angular frontend
2. WHEN the System starts in Docker THEN all services SHALL be properly networked and able to communicate
3. WHEN the System starts in development mode THEN hot-reload SHALL be enabled for backend and frontend code changes
4. WHEN the System starts in production mode THEN the System SHALL use optimized configurations and disable debug logging
5. WHEN the System is deployed THEN environment variables SHALL be configurable for database credentials, API endpoints, and other settings
6. WHEN the System starts THEN database migrations SHALL execute automatically before the application becomes available

### Requirement 10: API Contract and Integration

**User Story:** As a frontend developer, I want a well-defined API contract, so that I can integrate the frontend with the backend reliably.

#### Acceptance Criteria

1. THE System SHALL provide REST endpoints for file upload, data retrieval, and analysis status
2. WHEN a client calls the upload endpoint THEN the System SHALL return a JSON response with upload status and file ID
3. WHEN a client calls the data retrieval endpoint THEN the System SHALL return paginated JSON data with filtering and sorting parameters
4. WHEN a client calls the analysis endpoint THEN the System SHALL return analysis results in JSON format with metrics and insights
5. WHEN an API error occurs THEN the System SHALL return appropriate HTTP status codes (400, 404, 500) with error details in JSON format
