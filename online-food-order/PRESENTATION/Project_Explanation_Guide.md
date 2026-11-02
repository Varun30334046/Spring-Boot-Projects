# Comprehensive Project Explanation Guide: Online Food Order System

This guide covers all concepts, architectural decisions, Java classes, annotations, and API mechanics in simple, clear English. Use this guide to deeply understand and explain the project during your presentation.

---

## 1. What is this Project?

The **Online Food Order System** is a backend RESTful web service built with **Spring Boot**. It acts as the core server engine for an online food delivery platform. 

It exposes standard web interfaces (REST APIs) through which clients (such as web browsers, mobile apps, or Postman) can:
- Place new food orders
- View all active orders
- Search for a specific order by its unique ID
- Perform a complete update on an order
- Perform a partial update (e.g., updating only the quantity or price)
- Cancel/delete an order

---

## 2. Why did we develop it?

As beginners in Java and Spring Boot, jumping directly into complex frameworks with multiple databases, security layers, and third-party tools is overwhelming. 

We developed this project to:
1. Master core **REST API architecture** and understand HTTP methods (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`).
2. Learn the **Layered Architecture** pattern (Model - Controller - Service - Exception).
3. Practice **Jakarta Bean Validation** to protect APIs from invalid client input.
4. Learn **Centralized Exception Handling** using Spring's `@RestControllerAdvice`.
5. Understand in-memory data structures (`ArrayList`) and thread safety (`synchronized`) in a web environment.

---

## 3. What problem does it solve?

In traditional web applications, frontends and backends are tightly coupled. With a REST API backend:
- The backend serves clean, structured **JSON data**.
- Any frontend—an Android app, iOS app, React web application, or third-party partner service—can consume the same food ordering endpoints.
- It eliminates invalid inputs early through automated validation before processing.
- It provides predictable HTTP status codes (`200 OK`, `201 Created`, `400 Bad Request`, `404 Not Found`) so clients know exactly what happened.

---

## 4. How does the application work?

Here is the step-by-step life cycle of an API request:

1. **Client Sends Request**: A client (e.g., Postman) sends an HTTP request (such as `POST /api/orders`) with a JSON body.
2. **Tomcat Dispatches**: Spring Boot's embedded Tomcat web server receives the request and forwards it to the Spring `DispatcherServlet`.
3. **Controller Intercepts**: `OrderController` matches the URL mapping (`/api/orders`) and HTTP method (`POST`).
4. **Validation**: Spring automatically validates the incoming data using annotations like `@NotBlank` and `@Min`. If invalid, `MethodArgumentNotValidException` is thrown immediately.
5. **Service Layer Execution**: If valid, the controller calls `OrderService.addOrder(order)`.
6. **In-Memory Storage**: The service generates an auto-incremented ID (`1, 2, 3...`) using `AtomicLong` and saves the order to an `ArrayList`.
7. **Response Serialization**: The controller wraps the order in a `ResponseEntity` with status `201 Created`. Jackson converts the Java `Order` object into JSON and sends it back to the client.
8. **Exception Handling**: If an error occurs anywhere (e.g., order ID not found), `GlobalExceptionHandler` intercepts it and returns a neat JSON error response.

---

## 5. Explain the Architecture

We used a **Layered Architecture** adhering to the **Separation of Concerns (SoC)** principle:

```
[ Client / Postman ]
         │ (HTTP / JSON)
         ▼
[ Controller Layer ] -> com.example.onlinefoodorder.controller.OrderController
         │ (Java Method Calls)
         ▼
[ Service Layer ]    -> com.example.onlinefoodorder.service.OrderService
         │ (Java Collections)
         ▼
[ In-Memory Storage] -> ArrayList<Order> in RAM
         │
         ▼ (When Errors Occur)
[ Exception Layer ]  -> com.example.onlinefoodorder.exception.GlobalExceptionHandler
```

### Why not put everything in the Controller?
- **Controller's Only Job**: Receive HTTP requests, validate input format, and return HTTP status codes.
- **Service's Only Job**: Business logic, ID generation, calculations, and data manipulation.
- **Separation Benefits**: Code is clean, maintainable, testable, and reusable.

---

## 6. Explain Every Java Class

### 1. `Order.java` (in `com.example.onlinefoodorder.model`)
- **Role**: The Data Model / Entity representing an order.
- **Fields**:
  - `Long orderId`: Auto-generated identifier.
  - `String customerName`: Name of customer (`@NotBlank`).
  - `String foodItem`: Food dish ordered (`@NotBlank`).
  - `Integer quantity`: Number of portions (`@NotNull`, `@Min(1)`).
  - `Double price`: Cost of the order (`@NotNull`, `@Positive`).
- **Constructors**: Default constructor (needed by Jackson JSON deserializer) and Parameterized constructor.
- **Methods**: Standard getters, setters, and `toString()`.

### 2. `OrderController.java` (in `com.example.onlinefoodorder.controller`)
- **Role**: REST API Endpoints Handler.
- **Annotated with**: `@RestController`, `@RequestMapping("/api/orders")`.
- **Methods**:
  - `getAllOrders()`: `GET /api/orders` -> returns 200 OK with list.
  - `getOrderById(id)`: `GET /api/orders/{id}` -> returns 200 OK or 404.
  - `addOrder(order)`: `POST /api/orders` -> returns 201 Created.
  - `updateOrder(id, order)`: `PUT /api/orders/{id}` -> returns 200 OK.
  - `deleteOrder(id)`: `DELETE /api/orders/{id}` -> returns 200 OK with message.
  - `patchOrder(id, patchData)`: `PATCH /api/orders/{id}` -> returns 200 OK.

### 3. `OrderService.java` (in `com.example.onlinefoodorder.service`)
- **Role**: Business logic and data management.
- **Annotated with**: `@Service`.
- **Key Attributes**:
  - `List<Order> orderList = new ArrayList<>()`: In-memory storage.
  - `AtomicLong idCounter = new AtomicLong(1)`: Thread-safe ID counter.
  - `Validator validator`: Jakarta validator for validating patched orders.
- **Methods**: `getAllOrders()`, `getOrderById()`, `addOrder()`, `updateOrder()`, `deleteOrder()`, `patchOrder()`. All methods are `synchronized` for thread safety.

### 4. `OrderNotFoundException.java` (in `com.example.onlinefoodorder.exception`)
- **Role**: Custom unchecked runtime exception.
- Thrown when an order lookup fails for a specific ID.

### 5. `ErrorResponse.java` (in `com.example.onlinefoodorder.exception`)
- **Role**: Data Transfer Object (DTO) for API errors.
- **Fields**: `timestamp`, `status`, `error`, `message`, `validationErrors`.
- Ensures all API errors look uniform and professional across the entire app.

### 6. `GlobalExceptionHandler.java` (in `com.example.onlinefoodorder.exception`)
- **Role**: Centralized exception interceptor.
- **Annotated with**: `@RestControllerAdvice`.
- Catches exceptions and returns clean HTTP responses (`400 Bad Request`, `404 Not Found`, `500 Internal Server Error`).

### 7. `OnlineFoodOrderApplication.java` (in `com.example.onlinefoodorder`)
- **Role**: Main Spring Boot entry point.
- **Annotated with**: `@SpringBootApplication`.
- Launches embedded Tomcat and enables component scanning.

---

## 7. Explain Important Spring Boot Annotations

| Annotation | Where it is used | What it does |
| :--- | :--- | :--- |
| `@SpringBootApplication` | `OnlineFoodOrderApplication` | Combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`. Starts Spring Boot. |
| `@RestController` | `OrderController` | Marks class as REST controller. Combines `@Controller` and `@ResponseBody` (methods return JSON directly). |
| `@RequestMapping("/api/orders")` | `OrderController` | Sets the base URL path for all endpoints in the controller. |
| `@GetMapping` | Controller methods | Maps HTTP GET requests (retrieving data). |
| `@PostMapping` | Controller methods | Maps HTTP POST requests (creating new resources). |
| `@PutMapping` | Controller methods | Maps HTTP PUT requests (full update of existing resource). |
| `@PatchMapping` | Controller methods | Maps HTTP PATCH requests (partial update of existing resource). |
| `@DeleteMapping` | Controller methods | Maps HTTP DELETE requests (deleting resource). |
| `@PathVariable` | Method parameters | Extracts values from the URL path (e.g., `{id}`). |
| `@RequestBody` | Method parameters | Binds incoming HTTP JSON body to a Java object using Jackson. |
| `@Valid` | Method parameters | Triggers Jakarta Bean Validation on the model before executing the method. |
| `@Service` | `OrderService` | Registers the class as a business service bean in Spring's IoC container. |
| `@Autowired` | Constructors | Automatically injects dependent Spring beans (Dependency Injection). |
| `@RestControllerAdvice` | `GlobalExceptionHandler` | Intercepts exceptions thrown by any controller and formats HTTP responses globally. |
| `@ExceptionHandler` | Exception methods | Specifies which Java Exception class a handler method will catch. |

---

## 8. Explain the Six REST APIs

| Operation | HTTP Method | Endpoint | Request Body | Response Status | Purpose |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **A. Get All** | `GET` | `/api/orders` | None | `200 OK` | Retrieves all food orders from memory. |
| **B. Get By ID** | `GET` | `/api/orders/{id}` | None | `200 OK` / `404 Not Found` | Searches for a specific order by ID. |
| **C. Add Order** | `POST` | `/api/orders` | Complete JSON | `201 Created` | Creates a new order; auto-assigns orderId. |
| **D. Update Order** | `PUT` | `/api/orders/{id}` | Complete JSON | `200 OK` / `404 Not Found` | Replaces all editable fields of an order. |
| **E. Delete Order** | `DELETE` | `/api/orders/{id}` | None | `200 OK` / `404 Not Found` | Removes order from memory. |
| **F. Partial Update**| `PATCH` | `/api/orders/{id}` | Partial JSON | `200 OK` / `400 Bad Request` / `404 Not Found` | Modifies only provided fields without touching others. |

---

## 9. Explain Validation Rules

Validation ensures data integrity before storing it:

1. `@NotBlank(message = "Customer name is required")`:
   - Checks that customer name is not null and contains at least one non-whitespace character.
2. `@NotBlank(message = "Food item cannot be empty")`:
   - Ensures food item is specified.
3. `@Min(value = 1, message = "Quantity must be at least 1")`:
   - Prevents zero or negative order quantities.
4. `@NotNull(message = "Price is mandatory")`:
   - Ensures a price is provided.
5. `@Positive(message = "Price must be positive and greater than zero")`:
   - Prevents negative prices and zero prices.

---

## 10. Explain Exception Handling

Without exception handling, Spring Boot would return ugly HTML error pages or internal stack traces with sensitive system information.

Our `GlobalExceptionHandler`:
- Catches `OrderNotFoundException` -> Returns `404 Not Found`.
- Catches `MethodArgumentNotValidException` -> Returns `400 Bad Request` with field names and specific error messages.
- Catches `ConstraintViolationException` -> Returns `400 Bad Request` for invalid PATCH values.
- Catches `HttpMessageNotReadableException` -> Returns `400 Bad Request` for invalid JSON syntax.
- Catches `MethodArgumentTypeMismatchException` -> Returns `400 Bad Request` for non-numeric IDs in URLs.
- Provides a generic fallback for any unexpected error (`500 Internal Server Error`).

---

## 11. How Data is Stored (In-Memory Storage)

- Data is stored in: `private final List<Order> orderList = new ArrayList<>();`.
- Auto-increment IDs are generated using: `private final AtomicLong idCounter = new AtomicLong(1);`.
- **Thread Safety**: Multiple HTTP requests run on different Tomcat worker threads. Using `synchronized` methods guarantees that concurrent additions, modifications, or deletions will not corrupt the `ArrayList`.
- **Lifecycle**: Data exists temporarily in RAM while the Spring Boot application runs. When the application stops, memory is cleared. No dummy data is seeded on startup.
