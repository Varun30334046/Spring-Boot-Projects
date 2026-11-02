# Comprehensive Viva Questions & Answers: Online Food Order System

This document contains **30 essential viva questions** that trainers frequently ask during Spring Boot project reviews, along with clear, simple, and technically accurate answers.

---

### Q1: What is Spring Boot and why do we use it?
**Answer**:  
Spring Boot is an open-source Java framework used to build production-ready, standalone Spring applications with minimal configuration. It provides:
1. **Auto-configuration**: Automatically configures dependencies based on what is in `pom.xml`.
2. **Embedded Server**: Comes with embedded Tomcat, so we do not need to install an external web server.
3. **Starter Dependencies**: Simplifies Maven dependency management with curated sets of libraries (e.g., `spring-boot-starter-webmvc`, `spring-boot-starter-validation`).

---

### Q2: What is a REST API?
**Answer**:  
REST stands for **Representational State Transfer**. A REST API is a standard architectural style for web communication where clients and servers exchange data over HTTP. In REST:
- Endpoints represent resources (e.g., `/api/orders`).
- Operations are performed using standard HTTP methods (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`).
- Data is typically formatted as JSON.

---

### Q3: What is the difference between GET and POST methods?
**Answer**:  
- **GET**: Used to **retrieve** data from the server. It should be safe and idempotent (does not modify server state). Parameters are sent in the URL query string or path.
- **POST**: Used to **create** a new resource on the server. It carries the data payload inside the HTTP request body and is not idempotent (calling it multiple times creates multiple resources).

---

### Q4: What is the difference between PUT and PATCH methods?
**Answer**:  
- **PUT**: Performs a **full replacement** of an existing resource. The client must supply all editable fields. Missing fields are typically overwritten or reset.
- **PATCH**: Performs a **partial update**. The client sends only the fields that need to be changed (for example, just `quantity`), leaving all other fields unchanged.

---

### Q5: What is `@RestController` and how is it different from `@Controller`?
**Answer**:  
- `@Controller` is the traditional Spring MVC annotation used to return HTML web views (e.g., JSP, Thymeleaf).
- `@RestController` is a convenience annotation that combines `@Controller` and `@ResponseBody`. It tells Spring Boot that every method in the class will return domain data (like Java objects) directly serialized into JSON or XML in the HTTP response body, rather than rendering an HTML page.

---

### Q6: What is the purpose of `@RequestMapping("/api/orders")`?
**Answer**:  
`@RequestMapping` defines the base URL path for all HTTP endpoints inside the controller. By placing `@RequestMapping("/api/orders")` at the class level, all handler methods automatically inherit that prefix (e.g., `/api/orders`, `/api/orders/{id}`), avoiding repetitive code.

---

### Q7: Explain `@GetMapping`, `@PostMapping`, `@PutMapping`, `@PatchMapping`, and `@DeleteMapping`.
**Answer**:  
These are shortcut annotations introduced in Spring 4.3 that combine `@RequestMapping` with specific HTTP methods:
- `@GetMapping`: Handles HTTP `GET` requests (retrieval).
- `@PostMapping`: Handles HTTP `POST` requests (creation).
- `@PutMapping`: Handles HTTP `PUT` requests (full update).
- `@PatchMapping`: Handles HTTP `PATCH` requests (partial update).
- `@DeleteMapping`: Handles HTTP `DELETE` requests (deletion).

---

### Q8: What does `@RequestBody` do?
**Answer**:  
`@RequestBody` binds the incoming HTTP request body (JSON payload) to a Java object method parameter. Under the hood, Spring uses the Jackson library to deserialize the JSON into the Java `Order` object.

---

### Q9: What does `@PathVariable` do?
**Answer**:  
`@PathVariable` extracts values directly from the URI path. For example, in `/api/orders/{id}`, if a client calls `/api/orders/5`, `@PathVariable Long id` assigns the value `5` to the method parameter.

---

### Q10: What is the role of `@Valid`?
**Answer**:  
`@Valid` comes from Jakarta Bean Validation. When placed on a method parameter (e.g., `@Valid @RequestBody Order order`), it instructs Spring Boot to execute all validation constraints declared on the `Order` class before running the controller method. If any rule fails, Spring automatically throws a `MethodArgumentNotValidException`.

---

### Q11: What is the difference between `@NotBlank`, `@NotNull`, and `@NotEmpty`?
**Answer**:  
- `@NotNull`: Checks that the value is not `null`. (Allows empty strings `""` and whitespace `"   "`).
- `@NotEmpty`: Checks that the value is not `null` and its length or size is greater than 0. (Allows whitespace `"   "`).
- `@NotBlank`: Checks that the value is not `null`, not empty, and contains at least one non-whitespace character. It is the strictest and best choice for text fields like `customerName` and `foodItem`.

---

### Q12: What does `@Min(value = 1)` do?
**Answer**:  
`@Min(value = 1)` ensures that a numeric field (such as `quantity`) is greater than or equal to `1`. In our project, it prevents invalid orders where a user tries to order `0` or negative quantities.

---

### Q13: What does `@Positive` do and how does it protect the price field?
**Answer**:  
`@Positive` guarantees that a numeric field has a value strictly greater than `0`. It prevents negative prices (`-50.0`) and zero prices (`0.0`), ensuring that every food item has a valid monetary cost.

---

### Q14: What is `@RestControllerAdvice` and why do we use it?
**Answer**:  
`@RestControllerAdvice` is an annotation used for **Centralized Exception Handling** across all controllers. Instead of writing try-catch blocks in every controller method:
- Any exception thrown by any controller bubbles up to `GlobalExceptionHandler`.
- It converts Java exceptions into standardized, readable JSON error responses with appropriate HTTP status codes.
- It prevents technical stack traces from leaking to clients.

---

### Q15: What is `@ExceptionHandler`?
**Answer**:  
`@ExceptionHandler` is placed on methods inside `@RestControllerAdvice` to declare which specific exception class that method should catch. For example, `@ExceptionHandler(OrderNotFoundException.class)` will only handle instances of `OrderNotFoundException`.

---

### Q16: What HTTP status codes did you use and what do they mean?
**Answer**:  
1. **200 OK**: The request succeeded (used for `GET`, `PUT`, `PATCH`, and `DELETE`).
2. **201 Created**: A new resource was successfully created on the server (used for `POST`).
3. **204 No Content**: The server successfully processed the request, but is returning no content in the response body (alternative status for `DELETE`).
4. **400 Bad Request**: The client sent invalid data, violated validation rules, or sent malformed JSON.
5. **404 Not Found**: The requested resource ID does not exist in memory.
6. **500 Internal Server Error**: An unexpected server-side exception occurred.

---

### Q17: Why did we use an `ArrayList` instead of a database?
**Answer**:  
The project objective was to learn and demonstrate core Spring Boot, REST API architecture, HTTP methods, and Jakarta validation without external infrastructure complexity. Using an in-memory `ArrayList` keeps the application lightweight, self-contained, and easy to run on any computer without needing MySQL, PostgreSQL, or database configuration.

---

### Q18: What are the main limitations of storing data in an `ArrayList`?
**Answer**:  
1. **No Persistence**: When the application stops or restarts, all stored data in RAM is lost.
2. **Memory Bounds**: Constrained by JVM heap size; cannot store millions of records.
3. **No Indexing**: Searches require $O(N)$ linear scans instead of fast B-Tree lookups.
4. **No ACID Transactions**: No commit/rollback support if a multi-step operation fails.
5. **Cannot Scale Horizontally**: If running multiple app instances behind a load balancer, each server has its own isolated list.

---

### Q19: Why are methods in `OrderService` marked `synchronized`?
**Answer**:  
In Spring Boot, the embedded Tomcat web server handles incoming HTTP requests concurrently using a thread pool. A standard Java `ArrayList` is **not thread-safe**. If two requests try to add, modify, or delete orders at the exact same millisecond, the list can experience race conditions or throw `ConcurrentModificationException`. Marking the service methods with `synchronized` ensures that only one thread can modify or read the list at a time, preventing data corruption.

---

### Q20: How are Order IDs generated?
**Answer**:  
We use an `AtomicLong idCounter = new AtomicLong(1)` in `OrderService`. When a new order is added, `idCounter.getAndIncrement()` atomically generates the next unique sequential number (`1, 2, 3...`) in a thread-safe manner.

---

### Q21: How does Jackson work in Spring Boot?
**Answer**:  
Jackson is the default JSON processing library included with Spring Web.
- **Deserialization**: When a client sends a JSON request, Jackson converts that JSON into a Java object (calls the default no-arg constructor and setter methods).
- **Serialization**: When a controller method returns a Java object (like `Order`), Jackson reads the getter methods and converts the object into a JSON string sent in the HTTP response.

---

### Q22: Why does the `Order` model need a default (no-argument) constructor?
**Answer**:  
The Jackson deserializer uses Java reflection to instantiate objects from incoming JSON. It first calls the default no-argument constructor `public Order() {}` to create an empty instance, and then invokes the setter methods to populate fields from the JSON keys. Without a default constructor, deserialization will fail.

---

### Q23: How did you implement partial updates (PATCH) without overwriting untouched fields?
**Answer**:  
In `OrderService.patchOrder(id, patchData)`:
1. We retrieve the existing order by ID.
2. We create a temporary merged copy where fields present in `patchData` override the old values, while null/omitted fields keep their existing values.
3. We validate the merged object using `jakarta.validation.Validator`.
4. If validation passes, we update the stored order's fields. If validation fails, we throw `ConstraintViolationException`, leaving the existing order untouched in memory.

---

### Q24: What happens if a user passes letters for an ID in the URL, like `/api/orders/abc`?
**Answer**:  
Spring tries to convert the string `"abc"` to a `Long`. Because `"abc"` is not a valid number, Spring throws a `MethodArgumentTypeMismatchException`. Our `GlobalExceptionHandler` intercepts this and returns an HTTP `400 Bad Request` with the message: *"Parameter 'id' with value 'abc' is not a valid number."*

---

### Q25: What happens if the client sends malformed JSON (e.g. Missing quotation mark or syntax error)?
**Answer**:  
Jackson fails to parse the JSON and Spring throws an `HttpMessageNotReadableException`. Our `GlobalExceptionHandler` intercepts this and returns HTTP `400 Bad Request` with the message: *"Malformed JSON request body or invalid data type format."*

---

### Q26: What is Dependency Injection and where is it used in this project?
**Answer**:  
Dependency Injection (DI) is a design pattern where the Spring IoC container creates and provides the objects that a class needs, rather than the class creating them itself with `new`.  
In our project:
- `OrderService` is annotated with `@Service`.
- `OrderController` receives `OrderService` through its constructor using `@Autowired`. This decouples the controller from service creation.

---

### Q27: What is the Layered Architecture and why did we separate Controller and Service?
**Answer**:  
Layered architecture separates application responsibilities into distinct tiers:
- **Controller Layer**: Handles HTTP requests, URL mapping, and status codes.
- **Service Layer**: Contains business rules, data transformations, and storage logic.  
Separating them ensures single responsibility, improves testability, prevents business logic from being tied to the HTTP protocol, and allows changing the storage mechanism without modifying the controller.

---

### Q28: How do you run and test this project in Eclipse?
**Answer**:  
1. Import the project using **File -> Import -> Maven -> Existing Maven Projects**.
2. Locate `OnlineFoodOrderApplication.java` in `com.example.onlinefoodorder`.
3. Right-click -> **Run As -> Java Application** (or Spring Boot App).
4. Verify the console prints: *"Tomcat started on port 8080"*.
5. Open Postman, import `Online_Food_Order_API.postman_collection.json`, and run the requests.

---

### Q29: What is the purpose of `pom.xml` in Maven?
**Answer**:  
`pom.xml` (Project Object Model) is the central configuration file for Maven. It defines:
- Project coordinates (`groupId`, `artifactId`, `version`).
- The Spring Boot parent starter (`spring-boot-starter-parent`).
- Dependencies (`spring-boot-starter-webmvc`, `spring-boot-starter-validation`).
- Build plugins (`spring-boot-maven-plugin` to package the executable JAR).

---

### Q30: If you had to connect this project to MySQL tomorrow, what would change?
**Answer**:  
Because we used a layered architecture:
1. Add `spring-boot-starter-data-jpa` and `mysql-connector-j` to `pom.xml`.
2. Add `@Entity` and `@Id @GeneratedValue` to `Order.java`.
3. Create an interface `OrderRepository extends JpaRepository<Order, Long>`.
4. In `OrderService`, inject `OrderRepository` and replace the `ArrayList` with repository calls (`repository.findAll()`, `repository.save()`, `repository.deleteById()`).
5. Configure database URL, username, and password in `application.properties`.
6. Notice that `OrderController` and the REST API endpoints remain 100% unchanged!
