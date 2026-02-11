# Design Document: Smart-BPM Dashboard (VURPE)

## Overview

The Smart-BPM Dashboard (VURPE) is an enterprise full-stack application for analyzing business data through a hybrid ML + Rules Engine approach. The system enables users to upload CSV files, apply configurable business rules, and visualize insights through an interactive dashboard. The architecture follows Clean Architecture principles with clear separation of concerns across three main layers: backend (Spring Boot), database (H2), and frontend (Angular).

### Key Design Principles

- **Separation of Concerns**: Distinct services for ingestion, intelligence, and visualization
- **Asynchronous Processing**: Long-running operations don't block the UI
- **Data Integrity**: Strong validation at ingestion and persistence layers
- **Scalability**: Stateless services that can be horizontally scaled
- **Testability**: Clear contracts between components enable comprehensive testing

## Architecture

### System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        Frontend (Angular)                       │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Dashboard UI                                            │   │
│  │  - Data Table (paginated, sortable, filterable)          │   │
│  │  - Charts (bar, line, pie)                               │   │
│  │  - Upload Component                                      │   │
│  │  - Responsive Layout (desktop/tablet/mobile)             │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              ↕ (REST API + WebSocket)
┌─────────────────────────────────────────────────────────────────┐
│                    Backend (Spring Boot)                        │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  API Layer                                               │   │
│  │  - FileUploadController                                  │   │
│  │  - DataRetrievalController                               │   │
│  │  - AnalysisController                                    │   │
│  └──────────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Service Layer                                           │   │
│  │  - IngestionService (CSV parsing, validation)            │   │
│  │  - IntelligenceService (rules engine, metrics)           │   │
│  │  - AnalysisService (async task orchestration)            │   │
│  └──────────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Data Access Layer                                       │   │
│  │  - DataRecordRepository                                  │   │
│  │  - AnalysisResultRepository                              │   │
│  │  - BusinessRuleRepository                                │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
                              ↕ (JDBC)
┌─────────────────────────────────────────────────────────────────┐
│                  Database (H2)                                  │
│  - data_records table                                           │
│  - analysis_results table                                       │
│  - business_rules table                                         │
│  - async_tasks table                                            │
└─────────────────────────────────────────────────────────────────┘
```

### Technology Stack

**Backend**:
- Spring Boot 3.x (REST API, dependency injection, transaction management)
- Spring Data JPA (ORM, repository pattern)
- Spring Async (asynchronous task processing)
- Apache Commons CSV (CSV parsing)
- Lombok (boilerplate reduction)
- JUnit 5 + Mockito (testing)

**Database**:
- p (relational data persistence)
- Flyway (database migrations)
- JDBC (connection pooling via HikariCP)

**Frontend**:
- Angular 16+ (SPA framework)
- TypeScript (type-safe development)
- RxJS (reactive programming)
- Chart.js or ng2-charts (data visualization)
- Bootstrap 5 (responsive UI framework)
- Jasmine + Karma (testing)

**DevOps**:
- Docker & Docker Compose (containerization)
- Environment-based configuration

## Components and Interfaces

### Backend Components

#### 1. IngestionService

**Responsibility**: Validate and parse CSV files, store records in database

**Key Methods**:
```
uploadAndValidateCSV(file: MultipartFile): UploadResponse
  - Validates file format, size (≤50MB), required columns
  - Parses CSV and validates each record
  - Stores valid records in database
  - Returns upload status with file ID and error details

validateCSVStructure(file: MultipartFile): ValidationResult
  - Checks file format is valid CSV
  - Verifies required columns present: id, amount, category, date, description
  - Returns validation errors with row numbers

parseAndStoreRecords(file: MultipartFile): List<DataRecord>
  - Parses CSV rows into DataRecord objects
  - Assigns unique ID and timestamp to each record
  - Enforces data type constraints
  - Stores in H2
```

**Error Handling**:
- Invalid file format → HTTP 400 with error message
- File too large → HTTP 413 with size limit info
- Missing columns → HTTP 400 with column list
- Invalid data types → HTTP 400 with row numbers and field names

#### 2. IntelligenceService

**Responsibility**: Apply business rules, calculate metrics, generate insights

**Key Methods**:
```
applyBusinessRules(records: List<DataRecord>): List<DataRecord>
  - Loads all configured business rules
  - Evaluates each rule against each record
  - Assigns highest severity Risk_Flag when multiple rules match
  - Returns records with Risk_Flags assigned

calculateMetrics(records: List<DataRecord>): AnalysisResult
  - Calculates aggregate metrics: total amount, count, average amount
  - Calculates distribution by category and Risk_Flag
  - Calculates time-series aggregation by date
  - Stores results in database

evaluateRule(record: DataRecord, rule: BusinessRule): boolean
  - Evaluates rule condition against record
  - Supports conditions: amount comparisons, category matching
  - Returns true if rule matches
```

**Business Rules**:
- High Risk: amount > 10000 AND category = 'transfer'
- Medium Risk: amount > 5000 AND amount ≤ 10000 AND category = 'transfer'
- Low Risk: category = 'internal'

**Risk Flag Severity** (highest wins):
1. high_risk (severity: 3)
2. medium_risk (severity: 2)
3. low_risk (severity: 1)

#### 3. AnalysisService

**Responsibility**: Orchestrate asynchronous analysis tasks

**Key Methods**:
```
queueAnalysisTask(fileId: String): AsyncTaskResponse
  - Creates async task for analysis
  - Returns immediately with task ID
  - Queues task for sequential processing

processAnalysisTask(taskId: String): void
  - Retrieves records for task
  - Applies business rules
  - Calculates metrics
  - Updates task status to COMPLETED
  - Notifies frontend via WebSocket

getTaskStatus(taskId: String): AsyncTaskStatus
  - Returns current task status: QUEUED, PROCESSING, COMPLETED, FAILED
  - Returns results if completed
```

**Task Queue Management**:
- Sequential processing to prevent resource exhaustion
- Single-threaded executor for task processing
- Task status persisted in database

#### 4. API Controllers

**FileUploadController**:
```
POST /api/upload
  - Accepts multipart file upload
  - Calls IngestionService
  - Returns: { status: "success"|"error", fileId: string, errors?: [] }

GET /api/upload/{fileId}/status
  - Returns upload status and validation results
```

**DataRetrievalController**:
```
GET /api/data
  - Query parameters: page, size, sortBy, sortOrder, category, riskFlag, search
  - Returns paginated data records
  - Response: { data: DataRecord[], total: number, page: number, size: number }

GET /api/metrics
  - Returns calculated metrics and insights
  - Response: { aggregates: {}, distributions: {}, timeSeries: {} }
```

**AnalysisController**:
```
POST /api/analysis/{fileId}
  - Queues analysis task
  - Returns: { taskId: string, status: "QUEUED" }

GET /api/analysis/{taskId}
  - Returns task status and results
  - Response: { taskId: string, status: string, results?: AnalysisResult }

WebSocket /ws/analysis/{taskId}
  - Sends real-time updates when task completes
```

### Frontend Components

#### 1. Dashboard Component

**Responsibility**: Main container for all dashboard views

**Features**:
- Responsive layout (desktop/tablet/mobile)
- Manages overall state and data flow
- Coordinates between child components

#### 2. DataTableComponent

**Responsibility**: Display and interact with data records

**Features**:
- Paginated table display
- Sortable columns (id, amount, category, date, description, Risk_Flag)
- Filterable by category and Risk_Flag
- Real-time text search on description
- Responsive table layout

#### 3. ChartsComponent

**Responsibility**: Visualize metrics and insights

**Charts**:
- Bar chart: Record count by Risk_Flag
- Line chart: Total amount by date
- Pie chart: Distribution by category

**Features**:
- Updates reactively when filters change
- Responsive sizing

#### 4. FileUploadComponent

**Responsibility**: Handle CSV file uploads

**Features**:
- File input with validation
- Progress indicator
- Error message display
- Success notification

#### 5. FilterComponent

**Responsibility**: Manage data filters

**Features**:
- Category filter (dropdown)
- Risk_Flag filter (dropdown)
- Text search input
- Filter state management

## Data Models

### Database Schema

#### data_records Table
```sql
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
  FOREIGN KEY (file_id) REFERENCES files(id)
);

CREATE INDEX idx_data_records_file_id ON data_records(file_id);
CREATE INDEX idx_data_records_category ON data_records(category);
CREATE INDEX idx_data_records_risk_flag ON data_records(risk_flag);
CREATE INDEX idx_data_records_date ON data_records(date);
```

#### analysis_results Table
```sql
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
```

#### async_tasks Table
```sql
CREATE TABLE async_tasks (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  file_id UUID NOT NULL,
  status VARCHAR(50) NOT NULL,
  error_message TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  completed_at TIMESTAMP,
  FOREIGN KEY (file_id) REFERENCES files(id)
);
```

#### files Table
```sql
CREATE TABLE files (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  original_filename VARCHAR(255) NOT NULL,
  file_size BIGINT NOT NULL,
  upload_status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Java Data Models

#### DataRecord
```java
@Entity
@Table(name = "data_records")
public class DataRecord {
  @Id
  private UUID id;
  
  @Column(name = "file_id")
  private UUID fileId;
  
  @Column(name = "original_id")
  private String originalId;
  
  @Column(name = "amount")
  private BigDecimal amount;
  
  @Column(name = "category")
  private String category;
  
  @Column(name = "date")
  private LocalDateTime date;
  
  @Column(name = "description")
  private String description;
  
  @Column(name = "risk_flag")
  private String riskFlag;
  
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
```

#### AnalysisResult
```java
@Entity
@Table(name = "analysis_results")
public class AnalysisResult {
  @Id
  private UUID id;
  
  @Column(name = "file_id")
  private UUID fileId;
  
  @Column(name = "total_amount")
  private BigDecimal totalAmount;
  
  @Column(name = "record_count")
  private Integer recordCount;
  
  @Column(name = "average_amount")
  private BigDecimal averageAmount;
  
  @Column(name = "distribution_by_category", columnDefinition = "jsonb")
  private Map<String, Integer> distributionByCategory;
  
  @Column(name = "distribution_by_risk_flag", columnDefinition = "jsonb")
  private Map<String, Integer> distributionByRiskFlag;
  
  @Column(name = "time_series_by_date", columnDefinition = "jsonb")
  private Map<String, BigDecimal> timeSeriesByDate;
}
```

#### AsyncTask
```java
@Entity
@Table(name = "async_tasks")
public class AsyncTask {
  @Id
  private UUID id;
  
  @Column(name = "file_id")
  private UUID fileId;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TaskStatus status;
  
  @Column(name = "error_message")
  private String errorMessage;
  
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  
  @Column(name = "completed_at")
  private LocalDateTime completedAt;
}

enum TaskStatus {
  QUEUED, PROCESSING, COMPLETED, FAILED
}
```

### API Response Models

#### UploadResponse
```json
{
  "status": "success|error",
  "fileId": "uuid",
  "fileName": "string",
  "recordsProcessed": 0,
  "errors": [
    {
      "rowNumber": 0,
      "fieldName": "string",
      "message": "string"
    }
  ]
}
```

#### DataRetrievalResponse
```json
{
  "data": [
    {
      "id": "uuid",
      "originalId": "string",
      "amount": 0.00,
      "category": "string",
      "date": "2024-01-01T00:00:00Z",
      "description": "string",
      "riskFlag": "high_risk|medium_risk|low_risk"
    }
  ],
  "total": 0,
  "page": 0,
  "size": 20
}
```

#### MetricsResponse
```json
{
  "aggregates": {
    "totalAmount": 0.00,
    "recordCount": 0,
    "averageAmount": 0.00
  },
  "distributions": {
    "byCategory": { "category1": 0, "category2": 0 },
    "byRiskFlag": { "high_risk": 0, "medium_risk": 0, "low_risk": 0 }
  },
  "timeSeries": {
    "2024-01-01": 0.00,
    "2024-01-02": 0.00
  }
}
```

#### AsyncTaskResponse
```json
{
  "taskId": "uuid",
  "status": "QUEUED|PROCESSING|COMPLETED|FAILED",
  "results": null
}
```

## Correctness Properties

A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.

### Property 1: CSV Upload Validation

*For any* CSV file submitted to the upload endpoint, if the file is valid (correct format, size ≤ 50MB, contains all required columns), the system SHALL accept it and return a success response with a file ID.

**Validates: Requirements 1.1, 1.2, 1.3**

### Property 2: CSV Parsing Round Trip

*For any* valid CSV file with N records, after uploading and parsing, the system SHALL store exactly N records in the database with all fields preserved.

**Validates: Requirements 1.5, 1.6**

### Property 3: Record Uniqueness

*For any* set of records stored in the database, each record SHALL have a unique identifier and a timestamp assigned at storage time.

**Validates: Requirements 1.6, 2.4**

### Property 4: Data Type Enforcement

*For any* data record inserted into the database, the system SHALL enforce that amount is stored as decimal, date as timestamp, and category as string; invalid types SHALL be rejected.

**Validates: Requirements 2.3**

### Property 5: Query Performance

*For any* query against the database with up to 100,000 records, the system SHALL return results within 500ms.

**Validates: Requirements 2.5**

### Property 6: Business Rule Application

*For any* set of data records and a set of business rules, the system SHALL apply all rules to each record and assign the corresponding risk flags.

**Validates: Requirements 3.1, 3.2**

### Property 7: High Risk Rule Correctness

*For any* data record where amount > 10000 AND category = 'transfer', the system SHALL assign the risk flag 'high_risk'.

**Validates: Requirements 3.3**

### Property 8: Medium Risk Rule Correctness

*For any* data record where amount > 5000 AND amount ≤ 10000 AND category = 'transfer', the system SHALL assign the risk flag 'medium_risk'.

**Validates: Requirements 3.4**

### Property 9: Low Risk Rule Correctness

*For any* data record where category = 'internal', the system SHALL assign the risk flag 'low_risk'.

**Validates: Requirements 3.5**

### Property 10: Risk Flag Severity Priority

*For any* data record that matches multiple business rules, the system SHALL assign the highest severity risk flag (high_risk > medium_risk > low_risk).

**Validates: Requirements 3.7**

### Property 11: Async Task Queuing

*For any* file upload, the system SHALL queue an async analysis task and return immediately with a task ID without blocking the user.

**Validates: Requirements 8.1, 8.2**

### Property 12: Async Task Completion Notification

*For any* async task that completes, the system SHALL update the UI with results via WebSocket or polling.

**Validates: Requirements 8.3**

### Property 13: Sequential Task Processing

*For any* set of multiple async tasks queued, the system SHALL process them sequentially to prevent resource exhaustion.

**Validates: Requirements 8.5**

### Property 14: Metrics Calculation Correctness

*For any* set of data records, the system SHALL calculate aggregate metrics (total amount, record count, average amount) that match the mathematical sum and average of the input records.

**Validates: Requirements 4.1**

### Property 15: Distribution Metrics Correctness

*For any* set of data records, the system SHALL calculate distribution metrics by category and risk flag that correctly count records in each category/flag.

**Validates: Requirements 4.2**

### Property 16: Time Series Aggregation Correctness

*For any* set of data records with various dates, the system SHALL aggregate amounts by date correctly.

**Validates: Requirements 4.3**

### Property 17: Metrics Retrieval Performance

*For any* request for metrics, the system SHALL return results within 200ms.

**Validates: Requirements 4.5**

### Property 18: Table Sorting Functionality

*For any* column in the data table, when a user sorts by that column, the system SHALL return records sorted in ascending or descending order by that column.

**Validates: Requirements 5.2**

### Property 19: Table Filtering by Category

*For any* category filter applied to the data table, the system SHALL display only records matching the selected category.

**Validates: Requirements 5.3**

### Property 20: Table Filtering by Risk Flag

*For any* risk flag filter applied to the data table, the system SHALL display only records matching the selected risk flag.

**Validates: Requirements 5.3**

### Property 21: Text Search Filtering

*For any* text search query on the description field, the system SHALL filter records to show only those whose description contains the search text.

**Validates: Requirements 5.4**

### Property 22: Chart Update on Filter

*For any* filter applied to the data table, all charts (bar, line, pie) SHALL update to reflect the filtered dataset.

**Validates: Requirements 5.8**

### Property 23: Responsive Layout Desktop

*For any* dashboard viewed at desktop resolution (≥1200px), the system SHALL display components in a multi-column layout without breaking.

**Validates: Requirements 6.1**

### Property 24: Responsive Layout Tablet

*For any* dashboard viewed at tablet resolution (768px-1199px), the system SHALL display components in a two-column layout without breaking.

**Validates: Requirements 6.2**

### Property 25: Responsive Layout Mobile

*For any* dashboard viewed at mobile resolution (<768px), the system SHALL display components in a single-column layout without breaking.

**Validates: Requirements 6.3**

### Property 26: Layout Reflow on Resize

*For any* dashboard resize event, all charts and tables SHALL reflow without breaking layout or losing data.

**Validates: Requirements 6.4**

### Property 27: Upload Error Messages

*For any* failed file upload, the system SHALL display a user-friendly error message indicating the specific reason (invalid format, file too large, missing columns).

**Validates: Requirements 7.1**

### Property 28: Validation Error Details

*For any* data validation failure, the system SHALL provide specific row numbers and field names that caused the failure.

**Validates: Requirements 7.2**

### Property 29: API Response Format - Upload

*For any* call to the upload endpoint, the system SHALL return a JSON response with upload status and file ID.

**Validates: Requirements 10.2**

### Property 30: API Response Format - Data Retrieval

*For any* call to the data retrieval endpoint, the system SHALL return paginated JSON data with filtering and sorting parameters applied correctly.

**Validates: Requirements 10.3**

### Property 31: API Response Format - Analysis

*For any* call to the analysis endpoint, the system SHALL return analysis results in JSON format with metrics and insights.

**Validates: Requirements 10.4**

### Property 32: API Error Response Format

*For any* API error, the system SHALL return appropriate HTTP status codes (400, 404, 500) with error details in JSON format.

**Validates: Requirements 10.5**

## Error Handling

### File Upload Errors

| Error | HTTP Status | Response |
|-------|-------------|----------|
| Invalid file format | 400 | `{ "error": "Invalid CSV format", "details": "..." }` |
| File too large (>50MB) | 413 | `{ "error": "File exceeds 50MB limit", "size": 123456789 }` |
| Missing required columns | 400 | `{ "error": "Missing columns", "missing": ["id", "amount"] }` |
| Invalid data types | 400 | `{ "error": "Invalid data types", "errors": [{ "row": 5, "field": "amount", "message": "..." }] }` |

### Database Errors

| Error | Handling |
|-------|----------|
| Connection failure | Retry with exponential backoff; return 503 Service Unavailable |
| Constraint violation | Log error; return 400 Bad Request with details |
| Query timeout | Return 504 Gateway Timeout |

### Async Task Errors

| Error | Handling |
|-------|----------|
| Task processing failure | Store error in database; notify user via WebSocket; log for debugging |
| Resource exhaustion | Queue task; process sequentially when resources available |

## Testing Strategy

### Dual Testing Approach

The system uses both unit tests and property-based tests for comprehensive coverage:

- **Unit Tests**: Verify specific examples, edge cases, and error conditions
- **Property Tests**: Verify universal properties across all inputs using randomized test data

Both are complementary and necessary for complete correctness validation.

### Unit Testing

**Focus Areas**:
- Specific examples demonstrating correct behavior
- Integration points between components
- Edge cases and error conditions
- CSV parsing with various formats
- Business rule evaluation with boundary values
- API response formatting

**Test Framework**: JUnit 5 + Mockito (backend), Jasmine + Karma (frontend)

**Example Unit Tests**:
- Upload a valid CSV with 100 records → verify all stored
- Upload CSV with missing column → verify error response
- Apply high-risk rule to matching record → verify flag assigned
- Query with pagination → verify correct page returned
- Sort table by amount → verify ascending order

### Property-Based Testing

**Focus Areas**:
- Universal properties that hold for all inputs
- Comprehensive input coverage through randomization
- Round-trip properties (upload → parse → retrieve)
- Invariant properties (data integrity after operations)
- Metamorphic properties (relationships between operations)

**Test Framework**: QuickCheck (Haskell-style) or Hypothesis (Python-style) equivalent for Java

**Property Test Configuration**:
- Minimum 100 iterations per property test
- Each test references its design document property
- Tag format: `Feature: smart-bpm-dashboard, Property N: [property_text]`

**Example Property Tests**:
- For any valid CSV file, uploading and retrieving SHALL preserve all records
- For any set of records, calculating metrics SHALL match mathematical sum/average
- For any filter applied, filtered results SHALL only contain matching records
- For any async task, queuing SHALL return immediately with task ID
- For any risk flag assignment, highest severity SHALL be selected when multiple rules match

### Test Coverage Goals

- Backend: ≥80% code coverage
- Frontend: ≥70% component coverage
- Critical paths: 100% coverage (upload, rule evaluation, metrics)

