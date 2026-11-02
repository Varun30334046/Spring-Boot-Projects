# Class and Annotation Guide: Student Course Registration System

This guide provides a detailed, line-by-line breakdown of every Java class and annotation used in this project. Use this reference to answer detailed trainer questions.

---

# Part 1: Detailed Class Guide

### 1. `StudentCourseRegistrationApplication.java`
- **Package**: `com.example.studentcourseregistration`
- **Role**: Main application entry point.
- **Key Code**:
  ```java
  @SpringBootApplication
  public class StudentCourseRegistrationApplication {
      public static void main(String[] args) {
          SpringApplication.run(StudentCourseRegistrationApplication.class, args);
      }
  }
  ```
- **Explanation**: Contains the standard `main` method that Java executes. Calling `SpringApplication.run(...)` starts the embedded Apache Tomcat web server on port 8080 and initializes the Spring ApplicationContext.

---

### 2. `Student.java`
- **Package**: `com.example.studentcourseregistration.model`
- **Role**: Data Model (POJO - Plain Old Java Object).
- **Fields**:
  - `Long studentId`: Auto-generated unique student identifier.
  - `String studentName`: Full name of the student (`@NotBlank`).
  - `String courseName`: Name of the registered course (`@NotBlank`).
  - `String email`: Email address (`@NotBlank`, `@Email`).
  - `Integer age`: Student age (`@NotNull`, `@Min(18)`).
- **Constructors**:
  - `public Student()`: Default constructor required by Jackson to create empty objects before setting field values from incoming JSON.
  - `public Student(Long, String, String, String, Integer)`: Parameterized constructor for easily instantiating full student objects.
- **Methods**: Standard getters and setters for all 5 fields, plus `toString()` for logging.

---

### 3. `StudentService.java`
- **Package**: `com.example.studentcourseregistration.service`
- **Role**: Business Logic & Storage Service.
- **Key Fields**:
  - `List<Student> studentList = new CopyOnWriteArrayList<>()`: Thread-safe in-memory storage.
  - `AtomicLong idGenerator = new AtomicLong(1)`: Thread-safe ID counter.
- **Key Methods**:
  - `registerStudent(Student student)`: Increments ID generator, sets the ID on the student object, adds to `studentList`, and returns the student.
  - `getAllStudents()`: Returns `new ArrayList<>(studentList)` to provide a safe copy of the list.
  - `getStudentById(Long studentId)`: Uses Java Streams (`.filter(...)`, `.findFirst()`) to find the matching student. If not found, throws `StudentNotFoundException`.
  - `deleteStudentById(Long studentId)`: Calls `getStudentById(studentId)` to verify the student exists (throwing 404 if not), then calls `studentList.remove(...)`.

---

### 4. `StudentController.java`
- **Package**: `com.example.studentcourseregistration.controller`
- **Role**: REST Controller (API Gateway).
- **Key Fields**:
  - `private final StudentService studentService`: Injected dependency.
- **Key Methods**:
  - `registerStudent(@Valid @RequestBody Student student)`: Maps to `POST /api/students`, returns HTTP `201 Created`.
  - `getAllStudents()`: Maps to `GET /api/students`, returns HTTP `200 OK`.
  - `getStudentById(@PathVariable Long id)`: Maps to `GET /api/students/{id}`, returns HTTP `200 OK`.
  - `deleteStudent(@PathVariable Long id)`: Maps to `DELETE /api/students/{id}`, returns HTTP `200 OK` with a confirmation map.

---

### 5. `StudentNotFoundException.java`
- **Package**: `com.example.studentcourseregistration.exception`
- **Role**: Custom Business Exception.
- **Extends**: `RuntimeException` (unchecked exception).
- **Purpose**: Thrown by the service when a student lookup or delete fails due to an invalid or non-existent ID.

---

### 6. `ErrorResponse.java`
- **Package**: `com.example.studentcourseregistration.exception`
- **Role**: Standardized Error Model.
- **Fields**:
  - `String timestamp`: Formatted date and time when the error occurred (`yyyy-MM-dd HH:mm:ss`).
  - `int status`: HTTP status code integer (e.g., 400, 404, 500).
  - `String error`: HTTP error name (e.g., "Bad Request", "Not Found").
  - `String message`: Descriptive error explanation.
  - `String path`: The requested URI (e.g., `/api/students/999`).
  - `Map<String, String> validationErrors`: Key-value pairs of field names and error messages (populated during validation failures).

---

### 7. `GlobalExceptionHandler.java`
- **Package**: `com.example.studentcourseregistration.exception`
- **Role**: Centralized Exception Interceptor.
- **Annotated with**: `@RestControllerAdvice`.
- **Methods**:
  - `handleStudentNotFound(...)`: Intercepts `StudentNotFoundException` -> returns HTTP 404.
  - `handleValidationErrors(...)`: Intercepts `MethodArgumentNotValidException` -> extracts all field errors into a map -> returns HTTP 400.
  - `handleInvalidJson(...)`: Intercepts `HttpMessageNotReadableException` -> returns HTTP 400.
  - `handleTypeMismatch(...)`: Intercepts `MethodArgumentTypeMismatchException` -> returns HTTP 400.
  - `handleGenericException(...)`: Catch-all fallback -> returns HTTP 500.

---

# Part 2: Detailed Annotations Guide

### Spring Boot Framework Annotations

| Annotation | Location | What It Does / Why We Use It |
| :--- | :--- | :--- |
| **`@SpringBootApplication`** | `StudentCourseRegistrationApplication` | Meta-annotation that combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. Boots the Spring context and scans for beans. |
| **`@RestController`** | `StudentController` | Marks the class as a RESTful web controller. Combines `@Controller` and `@ResponseBody`, ensuring all return values are serialized directly to JSON. |
| **`@RequestMapping("/api/students")`** | `StudentController` | Sets the base URL path for all endpoints in the controller. |
| **`@CrossOrigin(origins = "*")`** | `StudentController` | Enables Cross-Origin Resource Sharing (CORS) so frontend applications (Angular/React/HTML) running on different ports can call our API. |
| **`@Service`** | `StudentService` | Marks the class as a Spring Service Bean in the business layer. Tells Spring's component scanner to manage its lifecycle as a singleton. |
| **`@RestControllerAdvice`** | `GlobalExceptionHandler` | Specialization of `@ControllerAdvice` for REST APIs. Intercepts exceptions across all `@RestController` classes and writes JSON responses. |
| **`@ExceptionHandler(...)`** | `GlobalExceptionHandler` | Specifies which specific Java exception class a handler method is responsible for processing. |
| **`@PostMapping`** | `StudentController` | Maps HTTP POST requests to a handler method (used for creating resources). |
| **`@GetMapping`** | `StudentController` | Maps HTTP GET requests to a handler method (used for reading resources). |
| **`@DeleteMapping`** | `StudentController` | Maps HTTP DELETE requests to a handler method (used for deleting resources). |
| **`@RequestBody`** | `StudentController.registerStudent` | Binds the incoming HTTP request body (JSON) to a Java method parameter using Jackson deserialization. |
| **`@PathVariable("id")`** | `StudentController.getStudentById` | Extracts a dynamic URI template variable (e.g., `{id}` in `/api/students/5`) into a method parameter. |

---

### Jakarta Bean Validation Annotations

| Annotation | Location | What It Does / Why We Use It |
| :--- | :--- | :--- |
| **`@Valid`** | `StudentController.registerStudent` | Placed on the method parameter to trigger validation on the incoming model before the method executes. |
| **`@NotBlank`** | `Student.studentName`, `courseName`, `email` | Ensures the string value is NOT null, NOT empty, and contains at least one non-whitespace character. |
| **`@Email`** | `Student.email` | Validates that the string matches standard email syntax (e.g., `user@domain.com`). |
| **`@NotNull`** | `Student.age` | Ensures that the field is not null (required for wrapper objects like `Integer`). |
| **`@Min(value = 18)`** | `Student.age` | Ensures that the numeric value is greater than or equal to 18. |
