# Implementation Plan: Smart-BPM Dashboard

## Overview

This implementation plan breaks down the Smart-BPM Dashboard into logical, manageable tasks following a layered approach: database schema and migrations, backend services, API controllers, frontend components, and integration. Each task builds incrementally on previous work, with property-based testing tasks integrated throughout to validate correctness properties early.

The implementation uses Spring Boot 3.x for the backend, PostgreSQL for persistence, and Angular 16+ for the frontend. Tasks are organized to enable parallel work on backend and frontend while maintaining clear integration points.

## Tasks

- [ ] 1. Project Setup and Infrastructure
  - [ ] 1.1 Initialize Spring Boot project with dependencies
    - Create Spring Boot 3.x project with Spring Data JPA, Spring Async, Apache Commons CSV
    - Configure application.properties for PostgreSQL connection
    - Set up Lombok and testing dependencies (JUnit 5, Mockito)
    - _Requirements: 2.1, 9.5_
  
  - [ ] 1.2 Initialize Angular project with dependencies
    - Create Angular 16+ project with TypeScript, RxJS, Chart.js/ng2-charts
    - Configure Bootstrap 5 for responsive UI
    - Set up testing framework (Jasmine + Karma)
    - _Requirements: 6.1, 6.2, 6.3_
  
  - [ ] 1.3 Create Docker Compose configuration
    - Define services for Spring Boot backend, PostgreSQL database, Angular frontend
    - Configure networking between services
    - Set up environment variables for development and production modes
    - _Requirements: 9.1, 9.2, 9.5_

- [ ] 2. Database Schema and Migrations
  - [ ] 2.1 Create Flyway migration for files table
    - Create files table with columns: id (UUID), original_filename, file_size, upload_status, created_at
    - Add primary key and indexes
    - _Requirements: 2.1, 2.2_
  
  - [ ] 2.2 Create Flyway migration for data_records table
    - Create data_records table with columns: id, file_id, original_id, amount, category, date, description, risk_flag, created_at, updated_at
    - Add foreign key to files table
    - Create indexes on file_id, category, risk_flag, date
    - _Requirements: 2.1, 2.2, 2.3, 2.4_
  
  - [ ] 2.3 Create Flyway migration for analysis_results table
    - Create analysis_results table with columns: id, file_id, total_amount, record_count, average_amount, distribution_by_category (JSONB), distribution_by_risk_flag (JSONB), time_series_by_date (JSONB), created_at
    - Add foreign key to files table
    - _Requirements: 2.1, 2.2, 4.4_
  
  - [ ] 2.4 Create Flyway migration for async_tasks table
    - Create async_tasks table with columns: id, file_id, status, error_message, created_at, completed_at
    - Add foreign key to files table
    - _Requirements: 2.1, 2.2, 8.1_

- [ ] 3. Backend Data Models and Repositories
  - [ ] 3.1 Create JPA entity classes
    - Implement DataRecord entity with all fields and annotations
    - Implement AnalysisResult entity with JSONB mapping
    - Implement AsyncTask entity with TaskStatus enum
    - Implement File entity
    - _Requirements: 2.3, 2.4_
  
  - [ ] 3.2 Create Spring Data JPA repositories
    - Create DataRecordRepository with custom query methods for filtering and pagination
    - Create AnalysisResultRepository
    - Create AsyncTaskRepository
    - Create FileRepository
    - _Requirements: 2.5_
  
  - [ ]* 3.3 Write property test for record uniqueness
    - **Property 3: Record Uniqueness**
    - **Validates: Requirements 1.6, 2.4**
    - Test that each stored record has unique ID and timestamp
  
  - [ ]* 3.4 Write property test for data type enforcement
    - **Property 4: Data Type Enforcement**
    - **Validates: Requirements 2.3**
    - Test that invalid data types are rejected during insertion

- [ ] 4. CSV Ingestion Service
  - [ ] 4.1 Implement IngestionService with CSV parsing
    - Create IngestionService class with uploadAndValidateCSV method
    - Implement CSV structure validation (format, required columns)
    - Implement CSV parsing using Apache Commons CSV
    - _Requirements: 1.1, 1.3, 1.5_
  
  - [ ] 4.2 Implement record validation and storage
    - Implement validateCSVStructure method with detailed error reporting
    - Implement parseAndStoreRecords method with data type validation
    - Assign unique IDs and timestamps to records
    - _Requirements: 1.4, 1.5, 1.6, 2.3_
  
  - [ ] 4.3 Implement file size and format validation
    - Add file size validation (≤50MB)
    - Add file format validation (CSV only)
    - Return appropriate error responses
    - _Requirements: 1.2_
  
  - [ ]* 4.4 Write property test for CSV upload validation
    - **Property 1: CSV Upload Validation**
    - **Validates: Requirements 1.1, 1.2, 1.3**
    - Test that valid CSV files are accepted and invalid ones rejected
  
  - [ ]* 4.5 Write property test for CSV parsing round trip
    - **Property 2: CSV Parsing Round Trip**
    - **Validates: Requirements 1.5, 1.6**
    - Test that N records in CSV result in N records in database with fields preserved

- [ ] 5. Business Rules Engine
  - [ ] 5.1 Create BusinessRule entity and repository
    - Implement BusinessRule JPA entity with rule definition and severity
    - Create BusinessRuleRepository
    - Seed default rules: high_risk, medium_risk, low_risk
    - _Requirements: 3.1, 3.3, 3.4, 3.5_
  
  - [ ] 5.2 Implement IntelligenceService with rule evaluation
    - Create IntelligenceService class
    - Implement evaluateRule method for condition matching
    - Implement applyBusinessRules method to apply all rules to records
    - _Requirements: 3.1, 3.2_
  
  - [ ] 5.3 Implement risk flag assignment with severity priority
    - Implement logic to assign highest severity risk flag when multiple rules match
    - Ensure severity order: high_risk (3) > medium_risk (2) > low_risk (1)
    - _Requirements: 3.7_
  
  - [ ]* 5.4 Write property test for high risk rule correctness
    - **Property 7: High Risk Rule Correctness**
    - **Validates: Requirements 3.3**
    - Test that records with amount > 10000 AND category = 'transfer' get high_risk flag
  
  - [ ]* 5.5 Write property test for medium risk rule correctness
    - **Property 8: Medium Risk Rule Correctness**
    - **Validates: Requirements 3.4**
    - Test that records with 5000 < amount ≤ 10000 AND category = 'transfer' get medium_risk flag
  
  - [ ]* 5.6 Write property test for low risk rule correctness
    - **Property 9: Low Risk Rule Correctness**
    - **Validates: Requirements 3.5**
    - Test that records with category = 'internal' get low_risk flag
  
  - [ ]* 5.7 Write property test for risk flag severity priority
    - **Property 10: Risk Flag Severity Priority**
    - **Validates: Requirements 3.7**
    - Test that highest severity flag is assigned when multiple rules match

- [ ] 6. Metrics Calculation Service
  - [ ] 6.1 Implement metrics calculation methods
    - Implement calculateMetrics method in IntelligenceService
    - Calculate aggregate metrics: total amount, record count, average amount
    - _Requirements: 4.1_
  
  - [ ] 6.2 Implement distribution metrics calculation
    - Calculate distribution by category (count per category)
    - Calculate distribution by risk flag (count per flag)
    - _Requirements: 4.2_
  
  - [ ] 6.3 Implement time-series aggregation
    - Aggregate amounts by date
    - Store time-series data in JSONB format
    - _Requirements: 4.3_
  
  - [ ] 6.4 Implement metrics persistence
    - Store calculated metrics in analysis_results table
    - _Requirements: 4.4_
  
  - [ ]* 6.5 Write property test for metrics calculation correctness
    - **Property 14: Metrics Calculation Correctness**
    - **Validates: Requirements 4.1**
    - Test that aggregate metrics match mathematical sum and average
  
  - [ ]* 6.6 Write property test for distribution metrics correctness
    - **Property 15: Distribution Metrics Correctness**
    - **Validates: Requirements 4.2**
    - Test that distribution counts are accurate for categories and risk flags
  
  - [ ]* 6.7 Write property test for time series aggregation correctness
    - **Property 16: Time Series Aggregation Correctness**
    - **Validates: Requirements 4.3**
    - Test that amounts are correctly aggregated by date

- [ ] 7. Asynchronous Task Processing
  - [ ] 7.1 Configure Spring Async for task processing
    - Create AsyncConfiguration with ThreadPoolTaskExecutor
    - Configure single-threaded executor for sequential processing
    - _Requirements: 8.5_
  
  - [ ] 7.2 Implement AnalysisService with task queuing
    - Create AnalysisService class
    - Implement queueAnalysisTask method to create and queue tasks
    - Return immediately with task ID
    - _Requirements: 8.1, 8.2_
  
  - [ ] 7.3 Implement async task processing
    - Implement processAnalysisTask method with @Async annotation
    - Retrieve records, apply rules, calculate metrics
    - Update task status to COMPLETED
    - _Requirements: 3.6, 8.1_
  
  - [ ] 7.4 Implement task status retrieval
    - Implement getTaskStatus method
    - Return task status: QUEUED, PROCESSING, COMPLETED, FAILED
    - Return results if completed
    - _Requirements: 8.3_
  
  - [ ] 7.5 Implement error handling for async tasks
    - Catch exceptions during task processing
    - Store error message in database
    - Update task status to FAILED
    - _Requirements: 8.4_
  
  - [ ]* 7.6 Write property test for async task queuing
    - **Property 11: Async Task Queuing**
    - **Validates: Requirements 8.1, 8.2**
    - Test that queuing returns immediately with task ID
  
  - [ ]* 7.7 Write property test for sequential task processing
    - **Property 13: Sequential Task Processing**
    - **Validates: Requirements 8.5**
    - Test that multiple tasks are processed sequentially

- [ ] 8. Backend API Controllers
  - [ ] 8.1 Implement FileUploadController
    - Create POST /api/upload endpoint
    - Call IngestionService.uploadAndValidateCSV
    - Return UploadResponse with status, fileId, errors
    - _Requirements: 1.1, 10.1, 10.2_
  
  - [ ] 8.2 Implement GET /api/upload/{fileId}/status endpoint
    - Return upload status and validation results
    - _Requirements: 1.1_
  
  - [ ] 8.3 Implement DataRetrievalController
    - Create GET /api/data endpoint with pagination, sorting, filtering
    - Support query parameters: page, size, sortBy, sortOrder, category, riskFlag, search
    - Return DataRetrievalResponse with paginated records
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 10.3_
  
  - [ ] 8.4 Implement GET /api/metrics endpoint
    - Return calculated metrics and insights
    - Return MetricsResponse with aggregates, distributions, timeSeries
    - _Requirements: 4.1, 4.2, 4.3, 10.4_
  
  - [ ] 8.5 Implement AnalysisController
    - Create POST /api/analysis/{fileId} endpoint
    - Call AnalysisService.queueAnalysisTask
    - Return AsyncTaskResponse with taskId and status
    - _Requirements: 8.1, 8.2, 10.1_
  
  - [ ] 8.6 Implement GET /api/analysis/{taskId} endpoint
    - Return task status and results
    - _Requirements: 8.3, 10.4_
  
  - [ ] 8.7 Implement WebSocket endpoint for real-time updates
    - Create WebSocket /ws/analysis/{taskId} endpoint
    - Send updates when task completes
    - _Requirements: 8.3_
  
  - [ ] 8.8 Implement global error handling
    - Create @ControllerAdvice for exception handling
    - Return appropriate HTTP status codes and error details
    - _Requirements: 7.1, 7.2, 10.5_
  
  - [ ]* 8.9 Write unit tests for API controllers
    - Test upload endpoint with valid and invalid files
    - Test data retrieval with various filters and sorting
    - Test analysis endpoint task queuing
    - _Requirements: 1.1, 5.1, 8.1_

- [ ] 9. Checkpoint - Backend Core Functionality
  - Ensure all backend tests pass (unit and property tests)
  - Verify database migrations execute successfully
  - Test API endpoints manually with Postman or curl
  - Ask the user if questions arise

- [ ] 10. Frontend Project Structure and Services
  - [ ] 10.1 Create Angular service for API communication
    - Create DataService for HTTP calls to backend
    - Implement methods for upload, data retrieval, metrics, analysis
    - Handle error responses
    - _Requirements: 10.1, 10.2, 10.3, 10.4_
  
  - [ ] 10.2 Create models and interfaces for API responses
    - Create TypeScript interfaces for UploadResponse, DataRetrievalResponse, MetricsResponse, AsyncTaskResponse
    - Create DataRecord, AnalysisResult models
    - _Requirements: 10.2, 10.3, 10.4_
  
  - [ ] 10.3 Create state management service
    - Create StateService to manage dashboard state
    - Track current filters, sorting, pagination
    - Track async task status
    - _Requirements: 5.1, 5.2, 5.3, 5.4_

- [ ] 11. Frontend Dashboard Component
  - [ ] 11.1 Create main Dashboard component
    - Create responsive layout container
    - Integrate child components: DataTable, Charts, FileUpload, Filters
    - Manage overall state and data flow
    - _Requirements: 5.1, 6.1, 6.2, 6.3_
  
  - [ ] 11.2 Implement responsive layout for desktop
    - Create multi-column layout for desktop (≥1200px)
    - Position DataTable and Charts side-by-side
    - _Requirements: 6.1_
  
  - [ ] 11.3 Implement responsive layout for tablet
    - Create two-column layout for tablet (768px-1199px)
    - Stack components appropriately
    - _Requirements: 6.2_
  
  - [ ] 11.4 Implement responsive layout for mobile
    - Create single-column layout for mobile (<768px)
    - Stack all components vertically
    - _Requirements: 6.3_
  
  - [ ] 11.5 Implement layout reflow on resize
    - Add window resize listener
    - Trigger layout recalculation
    - Ensure charts and tables reflow without breaking
    - _Requirements: 6.4_
  
  - [ ]* 11.6 Write property tests for responsive layouts
    - **Property 23: Responsive Layout Desktop**
    - **Property 24: Responsive Layout Tablet**
    - **Property 25: Responsive Layout Mobile**
    - **Property 26: Layout Reflow on Resize**
    - **Validates: Requirements 6.1, 6.2, 6.3, 6.4**

- [ ] 12. Frontend Data Table Component
  - [ ] 12.1 Create DataTable component with pagination
    - Display paginated table of data records
    - Show columns: id, amount, category, date, description, Risk_Flag
    - Implement page navigation
    - _Requirements: 5.1_
  
  - [ ] 12.2 Implement table sorting
    - Add sortable column headers
    - Support ascending/descending sort
    - Persist sort state
    - _Requirements: 5.2_
  
  - [ ] 12.3 Implement table filtering by category and risk flag
    - Add filter dropdowns for category and risk flag
    - Update table when filters change
    - _Requirements: 5.3_
  
  - [ ] 12.4 Implement text search on description
    - Add search input field
    - Filter records by description text in real-time
    - _Requirements: 5.4_
  
  - [ ]* 12.5 Write property tests for table functionality
    - **Property 18: Table Sorting Functionality**
    - **Property 19: Table Filtering by Category**
    - **Property 20: Table Filtering by Risk Flag**
    - **Property 21: Text Search Filtering**
    - **Validates: Requirements 5.2, 5.3, 5.4**

- [ ] 13. Frontend Charts Component
  - [ ] 13.1 Create Charts component with bar chart
    - Display bar chart showing record count by Risk_Flag
    - Use Chart.js or ng2-charts
    - _Requirements: 5.5_
  
  - [ ] 13.2 Implement line chart for time series
    - Display line chart showing total amount by date
    - _Requirements: 5.6_
  
  - [ ] 13.3 Implement pie chart for category distribution
    - Display pie chart showing distribution by category
    - _Requirements: 5.7_
  
  - [ ] 13.4 Implement chart updates on filter changes
    - Subscribe to filter changes
    - Recalculate chart data
    - Update charts reactively
    - _Requirements: 5.8_
  
  - [ ]* 13.5 Write property test for chart updates
    - **Property 22: Chart Update on Filter**
    - **Validates: Requirements 5.8**

- [ ] 14. Frontend File Upload Component
  - [ ] 14.1 Create FileUpload component
    - Create file input with drag-and-drop support
    - Validate file before upload
    - _Requirements: 1.1_
  
  - [ ] 14.2 Implement upload progress indicator
    - Show upload progress
    - Display success/error messages
    - _Requirements: 7.5_
  
  - [ ] 14.3 Implement error message display
    - Display user-friendly error messages
    - Show specific validation errors with row numbers and field names
    - _Requirements: 7.1, 7.2_
  
  - [ ] 14.4 Implement success notification
    - Display success message after upload
    - Show file ID and record count
    - _Requirements: 7.4_

- [ ] 15. Frontend Filter Component
  - [ ] 15.1 Create Filter component
    - Create category filter dropdown
    - Create risk flag filter dropdown
    - Create text search input
    - _Requirements: 5.3, 5.4_
  
  - [ ] 15.2 Implement filter state management
    - Emit filter changes to parent component
    - Persist filter state
    - _Requirements: 5.3, 5.4_

- [ ] 16. Frontend Async Task Monitoring
  - [ ] 16.1 Implement task status polling
    - Poll /api/analysis/{taskId} endpoint
    - Update UI with task status
    - _Requirements: 8.3_
  
  - [ ] 16.2 Implement WebSocket connection for real-time updates
    - Connect to /ws/analysis/{taskId}
    - Listen for task completion events
    - Update UI immediately
    - _Requirements: 8.3_
  
  - [ ] 16.3 Implement task error handling
    - Display error notification if task fails
    - Show error details
    - _Requirements: 8.4, 7.3_
  
  - [ ]* 16.4 Write property test for async task completion notification
    - **Property 12: Async Task Completion Notification**
    - **Validates: Requirements 8.3**

- [ ] 17. Checkpoint - Frontend Core Functionality
  - Ensure all frontend tests pass (unit and property tests)
  - Verify all components render correctly
  - Test responsive layouts on different screen sizes
  - Ask the user if questions arise

- [ ] 18. Integration and End-to-End Testing
  - [ ] 18.1 Test complete upload flow
    - Upload CSV file through UI
    - Verify records stored in database
    - Verify metrics calculated
    - _Requirements: 1.1, 1.5, 4.1_
  
  - [ ] 18.2 Test complete analysis flow
    - Queue analysis task
    - Monitor task status
    - Verify results displayed in UI
    - _Requirements: 8.1, 8.2, 8.3_
  
  - [ ] 18.3 Test data retrieval with filters and sorting
    - Apply various filters and sorts
    - Verify correct data returned
    - Verify charts update
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.8_
  
  - [ ] 18.4 Test error scenarios
    - Upload invalid CSV
    - Verify error messages displayed
    - Test network failures
    - _Requirements: 7.1, 7.2, 7.3_
  
  - [ ]* 18.5 Write integration tests
    - Test complete workflows end-to-end
    - Test API contract between frontend and backend
    - _Requirements: 10.1, 10.2, 10.3, 10.4, 10.5_

- [ ] 19. Docker Containerization and Deployment
  - [ ] 19.1 Create Dockerfile for Spring Boot backend
    - Multi-stage build for optimization
    - Expose port 8080
    - _Requirements: 9.1, 9.2_
  
  - [ ] 19.2 Create Dockerfile for Angular frontend
    - Build Angular application
    - Serve with nginx
    - Expose port 80
    - _Requirements: 9.1, 9.2_
  
  - [ ] 19.3 Configure Docker Compose for development
    - Enable hot-reload for backend and frontend
    - Configure volume mounts for source code
    - _Requirements: 9.3_
  
  - [ ] 19.4 Configure Docker Compose for production
    - Use optimized configurations
    - Disable debug logging
    - _Requirements: 9.4_
  
  - [ ] 19.5 Configure environment variables
    - Database credentials
    - API endpoints
    - Other settings
    - _Requirements: 9.5_
  
  - [ ] 19.6 Verify database migrations execute on startup
    - Ensure Flyway runs before application starts
    - _Requirements: 9.6, 2.2_

- [ ] 20. Performance Optimization and Testing
  - [ ] 20.1 Optimize database queries
    - Add appropriate indexes
    - Verify query performance
    - _Requirements: 2.5_
  
  - [ ] 20.2 Optimize metrics retrieval
    - Cache metrics results
    - Verify response time < 200ms
    - _Requirements: 4.5_
  
  - [ ]* 20.3 Write property test for query performance
    - **Property 5: Query Performance**
    - **Validates: Requirements 2.5**
    - Test that queries return within 500ms for 100k records
  
  - [ ]* 20.4 Write property test for metrics retrieval performance
    - **Property 17: Metrics Retrieval Performance**
    - **Validates: Requirements 4.5**
    - Test that metrics return within 200ms

- [ ] 21. Final Checkpoint - All Tests Pass
  - Ensure all unit tests pass
  - Ensure all property-based tests pass
  - Verify code coverage meets targets (backend ≥80%, frontend ≥70%)
  - Verify all critical paths have 100% coverage
  - Ask the user if questions arise

- [ ] 22. Documentation and Deployment Readiness
  - [ ] 22.1 Create API documentation
    - Document all REST endpoints
    - Include request/response examples
    - _Requirements: 10.1, 10.2, 10.3, 10.4, 10.5_
  
  - [ ] 22.2 Create deployment guide
    - Document Docker Compose setup
    - Document environment configuration
    - _Requirements: 9.1, 9.2, 9.5_
  
  - [ ] 22.3 Create user guide
    - Document how to upload CSV files
    - Document how to use dashboard features
    - Document how to interpret metrics
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7_

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP, but are recommended for comprehensive correctness validation
- Each task references specific requirements for traceability
- Property-based tests are integrated throughout to catch correctness issues early
- Checkpoints at tasks 9, 17, and 21 ensure incremental validation
- Backend and frontend can be developed in parallel after task 3
- All tasks assume access to the requirements.md and design.md documents for detailed specifications
