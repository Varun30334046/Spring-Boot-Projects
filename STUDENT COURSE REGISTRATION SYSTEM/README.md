# Student Course Registration System - REST API

A clean, beginner-friendly **Student Course Registration REST API** built with **Java 25**, **Spring Boot 4.0.9**, and **Maven**. This project demonstrates core Spring Boot concepts such as layered architecture, Bean Validation (`@Valid`, `@NotBlank`, `@Email`, `@Min`), global exception handling (`@RestControllerAdvice`), and in-memory data management using Java Collections without an external database.

---

## 1. Project Introduction

The **Student Course Registration System** is an academic backend web application designed to allow students to register for academic courses over HTTP using standard RESTful APIs. It provides an intuitive interface for clients (such as Postman, web browsers, or mobile apps) to perform essential registration operations including adding new students, retrieving registered students, looking up specific student records by ID, and canceling/deleting registrations.

---

## 2. Project Objective

- Demonstrate how to build RESTful web services in Spring Boot from scratch.
- Understand the **Controller-Service-Model** layered architecture.
- Implement robust input validation using **Jakarta Bean Validation**.
- Handle application errors and exceptions centrally using `@RestControllerAdvice`.
- Manage state in memory using thread-safe Java Collections (`CopyOnWriteArrayList` and `AtomicLong`).
- Provide clean, production-standard code without unnecessary third-party dependencies (no Lombok).

---

## 3. Technologies Used

| Technology | Version / Tool | Purpose |
| :--- | :--- | :--- |
| **Java** | 25 (LTS) | Core programming language |
| **Spring Boot** | 4.0.9 | Rapid application development framework |
| **Spring Web MVC** | 7.0.x | REST API routing and HTTP request/response dispatching |
| **Spring Validation** | Hibernate Validator 9.0.x | Declarative input validation annotations |
| **Apache Maven** | 3.10.x (with Maven Wrapper `mvnw`) | Dependency management and build automation |
| **Embedded Apache Tomcat** | 11.0.x | Built-in HTTP web server (runs on port 8080) |
| **Eclipse IDE** | Current | Recommended development environment |
| **Postman** | v10+ | API testing and verification |

---

## 4. Folder and Package Structure

```text
STUDENT COURSE REGISTRATION SYSTEM/
│
├── .mvn/wrapper/                                  # Maven Wrapper files
├── src/
│   ├── main/
│   │   ├── java/com/example/studentcourseregistration/
│   │   │   │
│   │   │   ├── StudentCourseRegistrationApplication.java  # Main entry point (@SpringBootApplication)
│   │   │   │
│   │   │   ├── model/
│   │   │   │   └── Student.java                   # POJO representing student registration
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   └── StudentController.java         # REST Controller exposing HTTP endpoints
│   │   │   │
│   │   │   ├── service/
│   │   │   │   └── StudentService.java            # Business logic and in-memory list management
│   │   │   │
│   │   │   └── exception/
│   │   │       ├── StudentNotFoundException.java  # Custom runtime exception (HTTP 404)
│   │   │       ├── ErrorResponse.java             # Structured JSON error response object
│   │   │       └── GlobalExceptionHandler.java    # Centralized exception handler (@RestControllerAdvice)
│   │   │
│   │   └── resources/
│   │       └── application.properties             # Port and application configurations
│   │
│   └── test/
│       └── java/com/example/studentcourseregistration/
│           └── StudentCourseRegistrationApplicationTests.java # 15 Automated Integration Tests
│
├── PRESENTATION/                                  # Complete presentation & interview preparation
│   ├── Student_Course_Registration_System.pptx    # Professional 10-slide PowerPoint Deck
│   ├── PRESENTATION_SLIDES.md                     # Complete slide deck transcript & visual layouts
│   ├── SPEAKING_SCRIPT.md                         # Natural presentation speech script
│   ├── PROJECT_EXPLANATION.md                     # In-depth architectural explanation
│   ├── CLASS_AND_ANNOTATION_GUIDE.md              # Line-by-line guide to classes & annotations
│   └── VIVA_QUESTIONS_AND_ANSWERS.md              # 30 Comprehensive viva Q&A
│
├── Student_Course_Registration_API.postman_collection.json # Ready-to-import Postman test collection
├── pom.xml                                        # Maven project configuration
└── README.md                                      # Project documentation (this file)
```

---

## 5. Model: `Student.java`

The model represents the student data structure. It contains exactly the trainer's required fields:

| Field Name | Java Type | Validation Rules | Description |
| :--- | :--- | :--- | :--- |
| `studentId` | `Long` | Auto-generated | Unique identifier (assigned by service) |
| `studentName` | `String` | `@NotBlank(message = "Student name cannot be empty")` | Student's full name |
| `courseName` | `String` | `@NotBlank(message = "Course name cannot be empty")` | Enrolled course |
| `email` | `String` | `@NotBlank(message = "Email cannot be empty")`<br>`@Email(message = "Enter valid email")` | Student contact email |
| `age` | `Integer` | `@NotNull(message = "Age cannot be null")`<br>`@Min(value = 18, message = "Age must be above 18")` | Student age (minimum 18) |

- **Constructors**: Contains a default constructor (`public Student()`) and a full parameterized constructor (`public Student(Long, String, String, String, Integer)`).
- **Getters and Setters**: Standard Java getters and setters are written explicitly for all fields (no Lombok dependency).

---

## 6. Service Layer: `StudentService.java`

The service layer contains business logic and acts as the data store:
- **In-Memory Storage**: Uses `CopyOnWriteArrayList<Student>` which is a thread-safe variant of `ArrayList`. Multiple incoming HTTP requests can read and write concurrently without data corruption.
- **Auto-Generated IDs**: Uses `AtomicLong idGenerator = new AtomicLong(1)` to produce sequential IDs (`1, 2, 3...`) in a thread-safe manner.
- **Methods**:
  - `registerStudent(Student student)`: Assigns a new ID, adds to collection, and returns the saved student.
  - `getAllStudents()`: Returns a snapshot list of all registered students.
  - `getStudentById(Long id)`: Finds student by ID or throws `StudentNotFoundException`.
  - `deleteStudentById(Long id)`: Finds student (validating existence) and removes it from the list.

---

## 7. Controller Layer: `StudentController.java`

The controller exposes the HTTP endpoints and maps URLs to service methods:
- Annotated with `@RestController` and `@RequestMapping("/api/students")`.
- Uses **Constructor-based Dependency Injection** to receive `StudentService`.
- Uses `@Valid` on request bodies to trigger Bean Validation automatically before executing method logic.

---

## 8. Validation Rules

Validation ensures only valid, safe data enters our application:
1. **Student Name**: Cannot be null, empty, or whitespace-only (`@NotBlank`).
2. **Email**: Cannot be empty (`@NotBlank`) and must follow standard email syntax (`@Email` e.g., `user@domain.com`).
3. **Age**: Cannot be null (`@NotNull`) and must be 18 or older (`@Min(18)`).
4. **Course Name**: Cannot be blank (`@NotBlank`).

When validation fails, Spring intercepts the request and delegates to `GlobalExceptionHandler`.

---

## 9. Exception Handling: `GlobalExceptionHandler.java`

Centralized error handling ensures all client errors receive clear, structured JSON responses:

### Standard Error JSON Format:
```json
{
  "timestamp": "2026-11-02 15:15:20",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for one or more fields",
  "path": "/api/students",
  "validationErrors": {
    "studentName": "Student name cannot be empty",
    "email": "Enter valid email",
    "age": "Age must be above 18"
  }
}
```

### Handled Exceptions:
- `StudentNotFoundException` -> **HTTP 404 Not Found**
- `MethodArgumentNotValidException` -> **HTTP 400 Bad Request** (returns field-specific error map)
- `HttpMessageNotReadableException` -> **HTTP 400 Bad Request** (malformed JSON syntax)
- `MethodArgumentTypeMismatchException` -> **HTTP 400 Bad Request** (string passed in place of numeric ID)
- `Exception` -> **HTTP 500 Internal Server Error** (unexpected system error)

---

## 10. The Four REST APIs

### A. Register Student
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/students`
- **Headers**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "studentName": "Varun",
  "courseName": "Java Full Stack",
  "email": "varun@gmail.com",
  "age": 22
}
```
- **Response (HTTP 201 Created)**:
```json
{
  "studentId": 1,
  "studentName": "Varun",
  "courseName": "Java Full Stack",
  "email": "varun@gmail.com",
  "age": 22
}
```

---

### B. View All Students
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/students`
- **Response (HTTP 200 OK)**:
```json
[
  {
    "studentId": 1,
    "studentName": "Varun",
    "courseName": "Java Full Stack",
    "email": "varun@gmail.com",
    "age": 22
  }
]
```

---

### C. Find Student by ID
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/students/{id}` (e.g., `/api/students/1`)
- **Response if Found (HTTP 200 OK)**:
```json
{
  "studentId": 1,
  "studentName": "Varun",
  "courseName": "Java Full Stack",
  "email": "varun@gmail.com",
  "age": 22
}
```
- **Response if Not Found (HTTP 404 Not Found)**:
```json
{
  "timestamp": "2026-11-02 15:15:20",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with ID: 999",
  "path": "/api/students/999",
  "validationErrors": null
}
```

---

### D. Delete Registration
- **Method**: `DELETE`
- **URL**: `http://localhost:8080/api/students/{id}` (e.g., `/api/students/1`)
- **Response if Found (HTTP 200 OK)**:
```json
{
  "message": "Student registration with ID 1 has been successfully deleted.",
  "status": "SUCCESS"
}
```
- **Response if Not Found (HTTP 404 Not Found)**:
```json
{
  "timestamp": "2026-11-02 15:15:20",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with ID: 999",
  "path": "/api/students/999",
  "validationErrors": null
}
```

---

## 11. How to Run in Eclipse IDE

1. **Open Eclipse IDE**.
2. Click **File -> Import...**.
3. Expand **Maven** and select **Existing Maven Projects**, then click **Next**.
4. Click **Browse...** and select this project folder:
   `C:\Users\varun urrinkala\Downloads\STUDENT COURSE REGISTRATION SYSTEM`
5. Ensure `pom.xml` is checked and click **Finish**.
6. Wait for Eclipse to finish building the workspace and downloading dependencies.
7. Locate `StudentCourseRegistrationApplication.java` under `src/main/java/com/example/studentcourseregistration/`.
8. Right-click the file -> **Run As -> Spring Boot App** (or **Run As -> Java Application**).
9. Check the Eclipse Console for the message:
   `Tomcat started on port 8080 (http)`

---

## 12. How to Test in Postman

1. Open the **Postman** desktop application.
2. Click the **Import** button in the top-left corner.
3. Drag and drop the file `Student_Course_Registration_API.postman_collection.json` located in the root of this project.
4. The imported collection contains two folders:
   - **1. Positive Test Cases**: Register Student, View All, Find by ID, Delete Registration.
   - **2. Negative Test Cases**: Empty Name, Invalid Email, Empty Email, Age < 18, Null Age, Missing ID, Invalid JSON, String ID.
5. Click each request and click the blue **Send** button.
6. Verify the HTTP Status code and JSON response body match the documented expectations.

---

## 13. In-Memory Storage Limitations

Since this academic project uses Java `CopyOnWriteArrayList` instead of a relational database (like MySQL or PostgreSQL):
1. **Volatile Storage**: All student records are stored in RAM. Whenever the Spring Boot application stops or restarts, all data is reset to empty.
2. **Memory Bounds**: Suitable for testing and academic demonstrations; large datasets in production would exceed JVM heap memory.
3. **No ACID Transactions**: Lacks transaction rollbacks and disk-backed consistency guarantees provided by relational databases.
4. **Transition to Production**: In a production environment, this in-memory list is typically replaced with Spring Data JPA (`JpaRepository<Student, Long>`) connected to MySQL or PostgreSQL without changing the Controller layer.
