# Viva Questions and Answers: Student Course Registration System

This document contains **30 comprehensive viva and interview questions** with concise, beginner-friendly answers directly related to the **Student Course Registration System**.

---

### General Spring Boot & Architecture

#### Q1: What is Spring Boot, and why did you use it for this project?
**Answer**: Spring Boot is an open-source Java framework that simplifies building stand-alone, production-ready Spring applications. We used it because it provides auto-configuration, starter dependencies, and an embedded Apache Tomcat web server, allowing us to build and run REST APIs with minimal setup.

#### Q2: What is the difference between `@Controller` and `@RestController`?
**Answer**: `@Controller` is used in traditional MVC web applications where methods return view names (like JSP or HTML templates). `@RestController` is a convenience annotation combining `@Controller` and `@ResponseBody`. In `@RestController`, methods automatically serialize return values directly into JSON format for RESTful API responses.

#### Q3: What is Layered Architecture, and why is it used here?
**Answer**: Layered architecture divides the application into separate logical tiers: Controller (HTTP handling), Service (business logic), and Model (data structure). This promotes the Single Responsibility Principle, makes code clean and maintainable, and allows swapping storage (e.g., from in-memory to MySQL) without touching controller code.

#### Q4: What is Dependency Injection (DI) and how is it used in your controller?
**Answer**: Dependency Injection is a design pattern where the Spring container automatically provides dependent objects rather than the class creating them manually using `new`. In our project, `StudentController` receives `StudentService` via **Constructor Injection**:
```java
public StudentController(StudentService studentService) {
    this.studentService = studentService;
}
```

#### Q5: What does the `@SpringBootApplication` annotation do?
**Answer**: `@SpringBootApplication` is a meta-annotation that bundles three core annotations:
1. `@Configuration`: Marks the class as a configuration source for Spring beans.
2. `@EnableAutoConfiguration`: Automatically configures libraries based on classpath dependencies.
3. `@ComponentScan`: Scans the current package and subpackages for components (`@RestController`, `@Service`, `@Component`).

---

### REST APIs & HTTP Protocol

#### Q6: What are the four HTTP methods used in REST APIs, and what do they represent?
**Answer**:
- **GET**: Read or retrieve resources.
- **POST**: Create a new resource.
- **PUT**: Replace or update an existing resource entirely.
- **DELETE**: Remove a resource.

#### Q7: What HTTP status codes are returned by your API endpoints?
**Answer**:
- **201 Created**: Returned by `POST /api/students` when a student is successfully registered.
- **200 OK**: Returned by `GET /api/students`, `GET /api/students/{id}`, and `DELETE /api/students/{id}` on success.
- **400 Bad Request**: Returned when input validation fails, JSON is malformed, or parameter types mismatch.
- **404 Not Found**: Returned when a requested student ID does not exist in the collection.
- **500 Internal Server Error**: Returned if an unhandled internal server error occurs.

#### Q8: What is the difference between `@PathVariable` and `@RequestParam`?
**Answer**:
- `@PathVariable` extracts values directly embedded in the URI path (e.g., `/api/students/1` where `1` is `{id}`).
- `@RequestParam` extracts values from query parameters appended after a question mark (e.g., `/api/students?course=Java`).

#### Q9: What does `@RequestBody` do?
**Answer**: `@RequestBody` instructs Spring to read the raw HTTP request body (JSON sent by the client) and deserialize it into a Java object (`Student`) using Jackson.

#### Q10: Why did you return HTTP 201 instead of 200 for student registration?
**Answer**: According to standard REST conventions, when an API creates a new resource on the server, it should return **201 Created**. HTTP **200 OK** is typically reserved for queries or operations that do not create new records.

---

### Model & Validation

#### Q11: What fields are in your `Student` model?
**Answer**: There are exactly five fields:
1. `studentId` (`Long`): Auto-generated unique identifier.
2. `studentName` (`String`): Student's full name.
3. `courseName` (`String`): Enrolled course.
4. `email` (`String`): Student's contact email.
5. `age` (`Integer`): Student's age.

#### Q12: Why did you write constructors, getters, and setters manually instead of using Lombok?
**Answer**: Avoiding Lombok ensures our code is 100% beginner-friendly, portable, and transparent. It demonstrates a clear understanding of Java encapsulation and standard JavaBean conventions without requiring IDE plugins or annotation processors.

#### Q13: What is the difference between `@NotNull`, `@NotEmpty`, and `@NotBlank`?
**Answer**:
- `@NotNull`: The value cannot be `null`, but it can be empty (`""`) or contain only whitespace (`"   "`).
- `@NotEmpty`: The value cannot be `null` and its length/size must be greater than 0, but it can contain only whitespace (`"   "`).
- `@NotBlank`: The strictest check. The string cannot be `null`, cannot be empty, and must contain at least one non-whitespace character.

#### Q14: How does `@Valid` work in your controller?
**Answer**: Placing `@Valid` before `@RequestBody Student student` triggers the Jakarta Bean Validation framework. Spring validates all constraints (`@NotBlank`, `@Email`, `@Min`, `@NotNull`) defined on `Student.java` before entering the controller method body. If any constraint fails, Spring throws `MethodArgumentNotValidException`.

#### Q15: Why did you use `@Min(18)` on the `age` field?
**Answer**: To enforce the academic requirement that students registering for courses must be at least 18 years old. If a client sends an age like `16`, validation immediately fails with the message *"Age must be above 18"*.

---

### In-Memory Storage & Concurrency

#### Q16: How are student records stored in your application?
**Answer**: Records are stored in memory using Java Collections. Specifically, we use `CopyOnWriteArrayList<Student>` in `StudentService`. No external database (such as MySQL or MongoDB) is used.

#### Q17: Why did you use `CopyOnWriteArrayList` instead of a regular `ArrayList`?
**Answer**: Spring Boot handles incoming HTTP requests concurrently using multiple threads. Standard `ArrayList` is not thread-safe and can lead to data inconsistency or `ConcurrentModificationException` during concurrent reads and writes. `CopyOnWriteArrayList` is a thread-safe implementation of `List`.

#### Q18: How is `studentId` generated?
**Answer**: We use `AtomicLong idGenerator = new AtomicLong(1)` in `StudentService`. When registering a new student, `idGenerator.getAndIncrement()` atomically provides the next unique sequential number (`1, 2, 3...`) in a thread-safe manner without ID collisions.

#### Q19: What is the main limitation of using an in-memory collection?
**Answer**: Volatility. All data is stored in the computer's RAM. If the Spring Boot application stops or restarts, all saved student registrations are permanently lost.

#### Q20: How would you migrate this project to use a real database in the future?
**Answer**: We would add `spring-boot-starter-data-jpa` and a database driver (like `mysql-connector-j`) in `pom.xml`, annotate `Student` with `@Entity` and `@Id`, and replace the in-memory list with an interface extending `JpaRepository<Student, Long>`. The Controller layer would require zero changes.

---

### Exception Handling

#### Q21: What is `@RestControllerAdvice`, and why did you use it?
**Answer**: `@RestControllerAdvice` is an annotation used to define a centralized, global exception handler for all REST controllers in the application. Instead of writing try-catch blocks in every controller method, exceptions bubble up to `GlobalExceptionHandler`, which formats them into clean JSON error responses.

#### Q22: What happens if a client requests a student ID that does not exist?
**Answer**: The service throws a custom `StudentNotFoundException`. Our `GlobalExceptionHandler` intercepts this exception using `@ExceptionHandler(StudentNotFoundException.class)` and returns an HTTP **404 Not Found** response with a clear message like *"Student not found with ID: 999"*.

#### Q23: How does your application format validation errors?
**Answer**: When validation fails, `GlobalExceptionHandler.handleValidationErrors()` intercepts `MethodArgumentNotValidException`. It loops through `ex.getBindingResult().getFieldErrors()` and collects each violated field name and its corresponding error message into a `validationErrors` map, returning HTTP **400 Bad Request**.

#### Q24: What happens if a client sends invalid JSON syntax?
**Answer**: Spring's message converter fails to parse the body and throws `HttpMessageNotReadableException`. Our global handler catches it and returns HTTP **400 Bad Request** with the message *"Malformed JSON request body or invalid data type provided"*.

---

### Testing & Tools

#### Q25: How did you test your application?
**Answer**: We tested the application using two methods:
1. **Automated Testing**: 15 JUnit 5 integration tests using `MockMvc` verifying all success flows, validation boundaries, and error handlers.
2. **Manual & API Testing**: A pre-configured **Postman Collection** testing both positive endpoints and negative test cases against a live running server on port 8080.

#### Q26: What negative test cases did you test in Postman?
**Answer**:
1. Empty student name (fails `@NotBlank` -> 400).
2. Invalid email format (fails `@Email` -> 400).
3. Empty email string (fails `@NotBlank` -> 400).
4. Age below 18 (fails `@Min(18)` -> 400).
5. Missing required age field (fails `@NotNull` -> 400).
6. Non-existing student ID (triggers `StudentNotFoundException` -> 404).
7. Delete non-existing student (triggers 404).
8. Malformed JSON syntax (triggers 400).
9. String passed in place of numeric ID (e.g. `/api/students/abc` -> 400).

#### Q27: What is Maven Wrapper (`mvnw`), and why is it useful?
**Answer**: The Maven Wrapper is a script (`mvnw.cmd` on Windows, `mvnw` on Linux/Mac) bundled with the project. It automatically downloads and uses the exact required version of Apache Maven, ensuring any developer can build and run the project immediately without manually installing Maven.

#### Q28: How do you run this project in Eclipse IDE?
**Answer**:
1. Open Eclipse -> **File -> Import... -> Existing Maven Projects**.
2. Select the project folder containing `pom.xml`.
3. Right-click `StudentCourseRegistrationApplication.java` -> **Run As -> Spring Boot App** (or **Run As -> Java Application**).
4. The application boots and listens on port 8080.

#### Q29: What is Jackson, and what role does it play here?
**Answer**: Jackson is the default JSON processing library included in Spring Boot Web. It automatically converts incoming JSON payloads into Java objects (deserialization) and outgoing Java objects into JSON strings (serialization).

#### Q30: What is CORS, and why is `@CrossOrigin(origins = "*")` included?
**Answer**: CORS stands for Cross-Origin Resource Sharing. Browsers block web pages from making HTTP requests to a different domain or port for security reasons. Adding `@CrossOrigin(origins = "*")` on our controller permits frontend web applications (e.g., running on port 3000 or 4200) to communicate with our backend API on port 8080 without browser CORS errors.
