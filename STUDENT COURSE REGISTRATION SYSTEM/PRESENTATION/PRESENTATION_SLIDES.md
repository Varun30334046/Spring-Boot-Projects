# Student Course Registration System - Slide Deck Transcript

This document outlines the exact content, structure, and visual layout of the 10-slide PowerPoint presentation (`Student_Course_Registration_System.pptx`).

---

## Slide 1: Title Slide (Dark Navy Theme)
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: Student Course Registration System
- **Subtitle**: A RESTful Web Service Built with Spring Boot & Java 25
- **Footer / Metadata**: Presenter: Varun | Architecture: Layered (Controller - Service - Model) | Storage: In-Memory Java Collection
- **Visual Design**: Premium Navy (#1A365D) background, cyan accent pillar, high-contrast white and soft blue typography.

---

## Slide 2: Project Overview & Key Objectives
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: Project Overview & Key Objectives
- **Left Card: Academic Scenario & Scope**:
  - **Student Course Registration**: Allows students to enroll in academic courses over standard HTTP REST protocols.
  - **CRUD Capabilities**: Supports student registration (POST), listing all students (GET), querying by ID (GET), and deletion (DELETE).
  - **No Database Dependency**: Uses in-memory Java Collection (`CopyOnWriteArrayList`) - perfect for lightweight academic demonstration.
  - **Beginner-Friendly Architecture**: Clear separation of Controller and Service responsibilities without Lombok clutter.
- **Right Card: Core Learning Objectives**:
  - **RESTful Design**: Implement standard HTTP methods (POST, GET, DELETE) and status codes (200, 201, 400, 404).
  - **Input Validation**: Enforce strict Jakarta validation annotations (`@NotBlank`, `@Email`, `@Min`, `@NotNull`).
  - **Centralized Exception Handling**: Use `@RestControllerAdvice` to convert runtime exceptions into friendly JSON responses.
  - **Postman API Verification**: Test both positive success paths and negative edge cases comprehensively.

---

## Slide 3: Technology Stack & Development Tools
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: Technology Stack & Development Tools
- **Grid Layout (6 Cards)**:
  1. **Java 25 (LTS)**: Core programming language with modern language features, performance improvements, and long-term stability.
  2. **Spring Boot 4.0.9**: Backend framework providing rapid auto-configuration, starter packs, and embedded Apache Tomcat.
  3. **Spring Web MVC**: REST API routing, request dispatching (`DispatcherServlet`), and JSON serialization with Jackson.
  4. **Jakarta Validation**: Declarative constraint validation (`@NotBlank`, `@Email`, `@Min`) powered by Hibernate Validator.
  5. **Apache Maven**: Build automation and dependency management with Maven Wrapper (`mvnw`).
  6. **Postman & Eclipse**: Eclipse IDE for writing clean code; Postman for manual testing of all HTTP request/response payloads.

---

## Slide 4: Layered Architecture & Separation of Concerns
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: Layered Architecture & Separation of Concerns
- **Tier 1: Client Layer (Postman / Browser)**:
  - Sends HTTP requests (GET, POST, DELETE) with JSON bodies.
- **Tier 2: Controller Layer (`StudentController.java`)**:
  - Intercepts requests at `/api/students`, validates request bodies using `@Valid`, and delegates business processing to `StudentService`.
- **Tier 3: Service Layer (`StudentService.java`)**:
  - Manages sequential unique ID generation (`AtomicLong`), executes business logic, and manages the collection.
- **Tier 4: Data Layer (In-Memory Java Collection)**:
  - `CopyOnWriteArrayList<Student>` stores student objects in RAM with thread-safety for concurrent HTTP calls.

---

## Slide 5: Data Model (`Student.java`) & Validation Constraints
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: Data Model (`Student.java`) & Validation Constraints
- **Table Summary**:
  | Field | Type | Validation Annotations | Error Message |
  | :--- | :--- | :--- | :--- |
  | `studentId` | `Long` | None (System Auto-generated) | Assigned automatically starting from 1 |
  | `studentName` | `String` | `@NotBlank` | *"Student name cannot be empty"* |
  | `courseName` | `String` | `@NotBlank` | *"Course name cannot be empty"* |
  | `email` | `String` | `@NotBlank` + `@Email` | *"Email cannot be empty"* / *"Enter valid email"* |
  | `age` | `Integer` | `@NotNull` + `@Min(18)` | *"Age cannot be null"* / *"Age must be above 18"* |

---

## Slide 6: The Four Required REST API Endpoints
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: The Four Required REST API Endpoints
- **Card A: Register Student**:
  - `POST /api/students` [HTTP 201 Created]
  - Accepts JSON payload, validates constraints, assigns auto-generated `studentId`, and saves student.
- **Card B: View All Students**:
  - `GET /api/students` [HTTP 200 OK]
  - Returns complete array of all currently registered students.
- **Card C: Find Student by ID**:
  - `GET /api/students/{id}` [HTTP 200 OK / 404 Not Found]
  - Returns student matching the URL ID, or HTTP 404 if not found.
- **Card D: Delete Registration**:
  - `DELETE /api/students/{id}` [HTTP 200 OK / 404 Not Found]
  - Deletes student from memory and returns a friendly confirmation message.

---

## Slide 7: Global Exception Handling Strategy
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: Global Exception Handling Strategy
- **Left Card: Handled Exceptions via `@RestControllerAdvice`**:
  - `StudentNotFoundException` -> HTTP 404 Not Found
  - `MethodArgumentNotValidException` -> HTTP 400 Bad Request (extracts field-by-field validation errors)
  - `HttpMessageNotReadableException` -> HTTP 400 Bad Request (malformed JSON syntax)
  - `MethodArgumentTypeMismatchException` -> HTTP 400 Bad Request (invalid parameter type)
  - `Exception` -> HTTP 500 Internal Server Error (fallback for unforeseen issues)
- **Right Card: Clean Error Response JSON**:
  - Structured fields: `timestamp`, `status`, `error`, `message`, `path`, and `validationErrors`.

---

## Slide 8: Postman Collection & Verification Results
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: Postman Collection & Verification Results
- **Left Card: 15 Automated Test Cases (100% Pass Rate)**:
  - Context loads cleanly.
  - All 4 positive endpoints verified with expected status codes (201, 200).
  - All negative validation cases tested (empty name, invalid email, empty email, age < 18, null age).
  - Malformed JSON and missing student ID cases verified.
- **Right Card: Postman Features**:
  - Dedicated pre-configured collection (`Student_Course_Registration_API.postman_collection.json`).
  - Separated into "Positive Test Cases" and "Negative Test Cases - Validation & Errors".
  - Verified live against running Tomcat server on port 8080.

---

## Slide 9: In-Memory Storage & Future Enhancements
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: In-Memory Storage & Future Enhancements
- **Left Card: Current In-Memory Design**:
  - Fast execution with zero external database installation.
  - Thread-safe using `CopyOnWriteArrayList` and `AtomicLong`.
  - Volatile storage in RAM (clears on restart, ideal for lab tests).
- **Right Card: Future Production Roadmap**:
  - Integrate Spring Data JPA with MySQL / PostgreSQL.
  - Separate `Course` into an independent entity with `@ManyToOne` relationship.
  - Add Spring Security with JWT token authentication.
  - Add Swagger / SpringDoc OpenAPI UI for browser-based interactive testing.

---

## Slide 10: Summary & Conclusion
- **Header**: STUDENT COURSE REGISTRATION SYSTEM
- **Title**: Summary & Conclusion
- **Key Takeaways**:
  1. Complete, functional REST API delivering all 4 required CRUD endpoints.
  2. Strict validation rules meeting trainer's exact requirements.
  3. Clean layered architecture without unnecessary Lombok complexity.
  4. 15 automated integration tests and ready-to-use Postman collection.
  5. Solid academic foundation for advanced Spring Boot microservices development.
- **Closing**: Thank You! Questions & Discussion are Welcome.
