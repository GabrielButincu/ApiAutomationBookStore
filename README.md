# Bookstore API Test Automation Framework

REST API test automation framework for the FakeRestAPI Bookstore, built with Java, REST Assured, and TestNG.

## Table of Contents
- [Technology Stack](#technology-stack)
- [Prerequisites & Installation](#prerequisites--installation)
- [Project Structure](#project-structure)
- [Framework Architecture](#framework-architecture)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [CI/CD Pipeline](#cicd-pipeline)
- [Test Coverage](#test-coverage)
- [Configuration](#configuration)

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Programming Language |
| Maven | 3.6+ | Build & Dependency Management |
| REST Assured | 5.3.2 | API Testing Framework |
| TestNG | 7.8.0 | Test Execution Framework |
| Allure | 2.24.0 | Advanced Test Reporting |
| ExtentReports | 5.1.1 | HTML Test Reporting |
| Jackson | 2.15.3 | JSON Serialization/Deserialization |
| AssertJ | 3.24.2 | Fluent Assertions |
| JavaFaker | 1.0.2 | Dynamic Test Data Generation |
| Lombok | 1.18.30 | Boilerplate Code Reduction |
| Owner | 1.0.12 | Configuration Management |
| SLF4J | 2.0.9 | Logging Framework |

## Prerequisites & Installation

### Step 1: Install Java JDK 17+

**Windows:**
1. Download JDK 17 or higher from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/)
2. Run the installer and follow the installation wizard
3. Set JAVA_HOME environment variable:
   - Open System Properties > Environment Variables
   - Create new System Variable:
     - Variable name: `JAVA_HOME`
     - Variable value: `C:\Program Files\Java\jdk-17` (or your installation path)
   - Add to PATH: `%JAVA_HOME%\bin`
4. Verify installation:
```bash
java -version
```

**macOS:**
```bash
brew install openjdk@17
```

**Linux:**
```bash
sudo apt-get install openjdk-17-jdk
```

### Step 2: Install Apache Maven 3.6+

**Windows:**
1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Set environment variables:
   - Variable name: `MAVEN_HOME`
   - Variable value: `C:\Program Files\Apache\maven`
   - Add to PATH: `%MAVEN_HOME%\bin`
4. Verify installation:
```bash
mvn -version
```

**macOS:**
```bash
brew install maven
```

**Linux:**
```bash
sudo apt-get install maven
```

### Step 3: Clone and Setup Project

```bash
git clone <repository-url>
cd bookstore-api-automation
mvn clean install -DskipTests
```

This will download all dependencies defined in `pom.xml`.

### Step 4: Verify Setup

```bash
mvn clean test
```

All 29 tests should pass successfully.

## Project Structure

```
bookstore-api-automation/
│
├── .github/
│   └── workflows/
│       └── api-tests.yml              # GitHub Actions CI/CD pipeline configuration
│
├── src/
│   ├── main/java/com/bookstore/api/
│   │   │
│   │   ├── clients/                   # API Client Layer (Service Objects)
│   │   │   ├── BaseApiClient.java     # Abstract base client with HTTP methods
│   │   │   ├── BooksApiClient.java    # Books API operations
│   │   │   └── AuthorsApiClient.java  # Authors API operations
│   │   │
│   │   ├── config/                    # Configuration Layer
│   │   │   ├── ApiConfig.java         # Configuration interface with Owner framework
│   │   │   └── ConfigurationManager.java  # Singleton config manager
│   │   │
│   │   ├── data/                      # Test Data Layer
│   │   │   └── TestDataFactory.java   # Factory for generating test data
│   │   │
│   │   ├── listeners/                 # TestNG Listeners
│   │   │   ├── TestListener.java      # Custom test execution listener
│   │   │   └── AllureListener.java    # Allure reporting listener
│   │   │
│   │   ├── models/                    # Data Models (POJOs)
│   │   │   ├── Book.java              # Book entity model
│   │   │   ├── Author.java            # Author entity model
│   │   │   └── ErrorResponse.java     # Error response model
│   │   │
│   │   ├── specs/                     # Request/Response Specifications
│   │   │   ├── RequestSpecs.java      # Reusable request specifications
│   │   │   └── ResponseSpecs.java     # Reusable response specifications
│   │   │
│   │   └── utils/                     # Utility Classes
│   │       ├── ApiAssertions.java     # Custom API assertions
│   │       └── JsonUtils.java         # JSON manipulation utilities
│   │
│   └── test/
│       ├── java/com/bookstore/api/tests/
│       │   │
│       │   ├── base/
│       │   │   └── BaseTest.java      # Base test class with common setup
│       │   │
│       │   ├── books/
│       │   │   └── BooksApiTest.java  # Books API test cases
│       │   │
│       │   └── authors/
│       │       └── AuthorsApiTest.java    # Authors API test cases
│       │
│       └── resources/
│           ├── config.properties      # Test configuration
│           ├── testng.xml            # TestNG suite configuration
│           ├── allure.properties     # Allure configuration
│           └── simplelogger.properties   # Logging configuration
│
├── target/                           # Maven build output (generated)
│   ├── allure-results/              # Allure test results
│   ├── site/allure-maven-plugin/    # Generated Allure reports
│   └── surefire-reports/            # TestNG XML reports
│
├── test-output/                     # TestNG output (generated)
│   └── extent-reports/              # ExtentReports HTML files
│
├── pom.xml                          # Maven project configuration
└── README.md                        # This file
```

## Framework Architecture

### Design Pattern: Service Object Pattern

#### 1. **Layered Architecture**

```
┌─────────────────────────────────────┐
│         Test Layer                  │  ← BooksApiTest, AuthorsApiTest
│  (What to test - Business logic)    │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│       API Client Layer              │  ← BooksApiClient, AuthorsApiClient
│  (How to interact with API)         │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│    Request Specification Layer      │  ← RequestSpecs, ResponseSpecs
│  (HTTP configuration & setup)       │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│       REST Assured                  │  ← HTTP Communication
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│       FakeRestAPI                   │  ← Target API
└─────────────────────────────────────┘
```

#### 2. **Key Components**

**BaseApiClient** - Abstract class providing common HTTP operations:
- GET, POST, PUT, DELETE methods
- Path parameter handling
- Query parameter support
- Allure step annotations

**Specific API Clients** - Extend BaseApiClient:
- `BooksApiClient` - All Books endpoint operations
- `AuthorsApiClient` - All Authors endpoint operations
- Each method represents one API operation

**Request Specifications** - Centralized configuration:
- Base URI and path configuration
- Content-Type and Accept headers
- Allure reporting filter
- Request/response logging

**Configuration Management** - Property-based config:
- `ApiConfig` interface with @DefaultValue
- `ConfigurationManager` singleton pattern
- Environment variable support
- Runtime override capability

**Test Data Factory** - Dynamic test data:
- Uses JavaFaker for realistic data
- Creates valid/invalid test objects
- Supports data without IDs (for creation)
- Random data generation

**Models** - POJO classes with:
- Lombok for boilerplate reduction
- Jackson for JSON mapping
- `@JsonInclude(NON_NULL)` to exclude null fields
- Builder pattern support

#### 3. **Configuration Flow**

```
config.properties → ApiConfig interface → ConfigurationManager → RequestSpecs → API Clients
```

Properties can be overridden at runtime:
```bash
mvn test -Dbase.uri=https://different-api.com
```

#### 4. **Test Execution Flow**

```
1. TestNG reads testng.xml
2. BaseTest.setupClass() initializes API clients
3. Test methods execute API operations via clients
4. Clients use RequestSpecs for HTTP configuration
5. REST Assured executes HTTP requests
6. Responses are validated with ApiAssertions
7. Results are logged to Allure and ExtentReports
```

## Running Tests

### Execute All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
mvn test -Dtest=AuthorsApiTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=AuthorsApiTest#testCreateAuthor_Success
```

### Run Multiple Test Classes
```bash
mvn test -Dtest=BooksApiTest,AuthorsApiTest
```

### Run with Custom Configuration
```bash
mvn test -Dbase.uri=https://fakerestapi.azurewebsites.net -Dlog.level=DEBUG
```

### Skip Tests
```bash
mvn clean install -DskipTests
```

## Test Reports

### 1. Allure Reports (Recommended)

**Generate and View:**
```bash
mvn allure:serve
```
This automatically opens the report in your browser.

**Generate Only:**
```bash
mvn allure:report
```
Report location: `target/site/allure-maven-plugin/index.html`

**Allure Features:**
- Interactive timeline and graphs
- Request/response details for each API call
- Test categorization by features/stories
- Attachments and screenshots
- Trend analysis across test runs
- Detailed error traces

### 2. ExtentReports

**Location:** `test-output/extent-reports/ExtentReport_*.html`

Open the HTML file in any browser for:
- Real-time test execution summary
- Pass/fail statistics with pie charts
- Test execution timeline
- System information
- Detailed test steps and logs

### 3. TestNG Reports

**Location:** `target/surefire-reports/`

XML and HTML reports generated automatically after test execution.

## CI/CD Pipeline

### GitHub Actions Workflow

The project includes a complete CI/CD pipeline (`.github/workflows/api-tests.yml`):

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop`
- Scheduled daily at 2 AM UTC
- Manual workflow dispatch

**Pipeline Stages:**
1. **Setup** - Checkout code, setup Java (17 & 21), cache Maven dependencies
2. **Test Execution** - Run all tests with `mvn clean test`
3. **Report Generation** - Generate Allure reports
4. **Artifact Upload** - Upload test results and reports
5. **PR Comments** - Auto-comment test results on pull requests
6. **GitHub Pages** - Deploy Allure reports (main branch only)

**Viewing Results:**
1. Navigate to repository > Actions tab
2. Select the workflow run
3. Download artifacts:
   - `allure-report-java-17` / `allure-report-java-21`
   - `extent-reports-java-17` / `extent-reports-java-21`
   - `test-results-java-17` / `test-results-java-21`

**Manual Trigger:**
1. Go to Actions > API Test Automation CI/CD
2. Click "Run workflow"
3. Select branch and run

**Live Allure Reports:**

Latest test reports are automatically published to GitHub Pages:
https://gabrielbutincu.github.io/ApiAutomationBookStore/

## Test Coverage

### Authors API - Complete Coverage

All 5 required endpoints are fully tested with happy paths and edge cases:

#### 1. GET /api/v1/Authors
**Tests:**
- `testGetAllAuthors_Success` - Retrieve all authors successfully
- `testGetAllAuthors_ResponseTime` - Validate response time < 2000ms
- `testGetAllAuthors_Headers` - Verify Content-Type header

**Coverage:** Happy path + performance + headers validation

#### 2. GET /api/v1/Authors/{id}
**Tests:**
- `testGetAuthorById_Success` - Retrieve specific author by valid ID
- `testGetAuthorById_NotFound` - 404 error for non-existent ID
- `testGetAuthorById_InvalidId` - Error handling for invalid ID (0)

**Coverage:** Happy path + not found + invalid input

#### 3. POST /api/v1/Authors
**Tests:**
- `testCreateAuthor_Success` - Create author with valid data
- `testCreateAuthor_AllFields` - Create with all fields populated
- `testCreateAuthor_InvalidData` - Validation with invalid data

**Coverage:** Happy path + full data + validation errors

#### 4. PUT /api/v1/Authors/{id}
**Tests:**
- `testUpdateAuthor_Success` - Update existing author successfully
- `testUpdateAuthor_PartialUpdate` - Partial field update
- `testUpdateAuthor_NotFound` - 404 error for non-existent author

**Coverage:** Happy path + partial update + not found

#### 5. DELETE /api/v1/Authors/{id}
**Tests:**
- `testDeleteAuthor_Success` - Delete existing author
- `testDeleteAuthor_NotFound` - 404 error for non-existent author

**Coverage:** Happy path + not found

**Additional Tests:**
- `testGetAuthorsByBookId_Success` - Get authors by book relationship

### Books API Coverage

14 comprehensive test cases covering:
- **GET /api/v1/Books** - List all books (success, performance, headers)
- **GET /api/v1/Books/{id}** - Get by ID (success, not found, invalid ID)
- **POST /api/v1/Books** - Create book (success, all fields, invalid data)
- **PUT /api/v1/Books/{id}** - Update book (success, partial update, not found)
- **DELETE /api/v1/Books/{id}** - Delete book (success, not found)

### Total Test Coverage
- **29 automated test cases**
- **100% endpoint coverage** for Books and Authors APIs
- **Happy path coverage:** All successful scenarios for CRUD operations
- **Edge case coverage:** Invalid IDs, not found scenarios, invalid data, partial updates
- **Non-functional testing:** Performance validation, headers verification

## Configuration

### config.properties

Located at: `src/test/resources/config.properties`

```properties
# API Configuration
base.uri=https://fakerestapi.azurewebsites.net
api.version=/api/v1
api.timeout=30000

# Endpoints
books.endpoint=/Books
authors.endpoint=/Authors

# Test Configuration
retry.count=2
parallel.execution=false
thread.count=5

# Logging
log.level=INFO
log.requests=true
log.responses=true

# Environment
test.environment=QA
```

### Runtime Override

Override any property:
```bash
mvn test -Dbase.uri=https://staging-api.com -Dlog.level=DEBUG
```

### testng.xml Configuration

Suite configuration for TestNG:
- Sequential execution (parallel=none)
- Test listeners for reporting
- Organized test groups

## Troubleshooting

### Tests Fail with NullPointerException
Ensure `config.properties` is in `src/test/resources/`

### Allure Report Not Generating
Install Allure command-line:
```bash
# Windows (Scoop)
scoop install allure

# macOS
brew install allure

# Or use Maven plugin
mvn allure:serve
```

### Maven Dependencies Not Downloading
Clear Maven cache:
```bash
mvn dependency:purge-local-repository
mvn clean install
```

### Tests Timing Out
Increase timeout in `config.properties`:
```properties
api.timeout=60000
```
