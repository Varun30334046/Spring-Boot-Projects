# Presentation Slides Content: Online Food Order System

---

## Slide 1: Title Slide
- **Title**: Online Food Order System
- **Subtitle**: Spring Boot RESTful Web Service with Jakarta Validation
- **Presenter**: Varun Urrinkala
- **Technology Stack**: Java 25 | Spring Boot | REST APIs | Jakarta Validation | Maven | Postman

---

## Slide 2: Project Introduction
- **What is it?**: A lightweight, robust backend service simulating online food ordering platforms (Swiggy / Zomato).
- **Core Functionality**:
  - Accepts customer food orders via standard HTTP REST APIs.
  - Automatically generates unique sequential order IDs.
  - Supports complete CRUD operations: Create, Read, Update (Full & Partial), and Delete.
  - Stores order data temporarily in memory using a Java `ArrayList` (no database required).

---

## Slide 3: Project Objectives
- **Master REST API Architecture**: Implement standard HTTP methods (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`).
- **Implement Layered Architecture**: Clear separation of concerns between Model, Controller, Service, and Exception handling.
- **Ensure Data Integrity**: Enforce strict server-side Jakarta Bean Validation.
- **Centralize Error Handling**: Uniform JSON error responses using `@RestControllerAdvice`.
- **Practice Postman Testing**: Verify all positive CRUD flows and negative edge cases.

---

## Slide 4: Technologies Used
| Technology | Role in Project |
| :--- | :--- |
| **Java 25 LTS** | Core programming language |
| **Spring Boot** | Application framework with embedded Tomcat server |
| **Spring Web MVC** | DispatcherServlet, REST Controllers, and JSON serialization |
| **Jakarta Validation** | Model annotations (`@NotBlank`, `@Min`, `@NotNull`, `@Positive`) |
| **Apache Maven** | Build automation and dependency management |
| **Eclipse IDE** | Project development and debugging environment |
| **Postman** | Comprehensive API endpoint testing and verification |

---

## Slide 5: Layered Architecture & Folder Structure
- **Layered Architecture Flow**:
  - `Client (Postman)` ➔ `Controller Layer (OrderController)` ➔ `Service Layer (OrderService)` ➔ `In-Memory ArrayList`
- **Package Breakdown (`com.example.onlinefoodorder`)**:
  - `model`: `Order.java` (Domain entity and validation rules)
  - `controller`: `OrderController.java` (REST endpoints and request routing)
  - `service`: `OrderService.java` (Business logic, ID generation, thread safety)
  - `exception`: `GlobalExceptionHandler.java`, `OrderNotFoundException.java`, `ErrorResponse.java`

---

## Slide 6: Model & Validation Rules
- **Fields in `Order.java`**:
  - `Long orderId`: Unique identifier (auto-generated)
  - `String customerName`: Customer placing order
  - `String foodItem`: Dish name ordered
  - `Integer quantity`: Number of portions
  - `Double price`: Cost of order
- **Exact Validation Annotations**:
  - `@NotBlank(message = "Customer name is required")`
  - `@NotBlank(message = "Food item cannot be empty")`
  - `@NotNull(message = "Quantity is mandatory")` & `@Min(value = 1, message = "Quantity must be at least 1")`
  - `@NotNull(message = "Price is mandatory")` & `@Positive(message = "Price must be positive and greater than zero")`

---

## Slide 7: Six Core REST API Endpoints
| Operation | Method | Endpoint | Expected Status | Description |
| :--- | :--- | :--- | :--- | :--- |
| **Get All Orders** | `GET` | `/api/orders` | `200 OK` | Retrieves all stored orders |
| **Get Order by ID** | `GET` | `/api/orders/{id}` | `200 OK` / `404 Not Found` | Searches order by unique ID |
| **Add New Order** | `POST` | `/api/orders` | `201 Created` | Creates order with auto-generated ID |
| **Update Order** | `PUT` | `/api/orders/{id}` | `200 OK` / `404 Not Found` | Full replacement of editable fields |
| **Delete Order** | `DELETE` | `/api/orders/{id}` | `200 OK` / `404 Not Found` | Removes order from memory |
| **Partial Update** | `PATCH` | `/api/orders/{id}` | `200 OK` / `400 Bad Request` | Updates only provided fields (e.g. quantity) |

---

## Slide 8: Exception Handling Strategy
- **Centralized Handling**: Implemented using `@RestControllerAdvice` in `GlobalExceptionHandler.java`.
- **Prevents Stack Traces**: Replaces ugly internal error traces with clean, uniform JSON objects.
- **Handled Scenarios**:
  - `OrderNotFoundException` ➔ `404 Not Found`
  - `MethodArgumentNotValidException` (POST/PUT validation) ➔ `400 Bad Request` with field error map
  - `ConstraintViolationException` (PATCH validation) ➔ `400 Bad Request`
  - `HttpMessageNotReadableException` (Malformed JSON) ➔ `400 Bad Request`
  - `MethodArgumentTypeMismatchException` (Non-numeric URL parameter) ➔ `400 Bad Request`
  - `Exception` (Unexpected server errors) ➔ `500 Internal Server Error`

---

## Slide 9: Postman Testing & Verification
- **Automated Test Suite**:
  - Maven tests: `mvn test` ➔ 6 tests run, 0 failures, `BUILD SUCCESS`.
- **Live Server Test Verification**:
  - 15 total test cases executed against live Spring Boot server (`http://localhost:8080`).
  - Tested all positive CRUD paths.
  - Tested negative scenarios: empty names, zero quantity, negative prices, missing fields, invalid IDs.
- **Postman Collection**: Pre-configured JSON collection provided for instant one-click testing.

---

## Slide 10: Conclusion & Key Learnings
- **Summary**: Successfully built, tested, and verified an Online Food Order REST API in Spring Boot.
- **Key Takeaways**:
  - Deep understanding of REST conventions and HTTP status codes (`200`, `201`, `400`, `404`).
  - Implementation of layered architecture and separation of concerns.
  - Practical usage of Jakarta Bean Validation and `@RestControllerAdvice`.
  - Managing in-memory data with thread-safe `synchronized` collections.
- **Ready for Production Transition**: Designed so in-memory storage can be swapped for Spring Data JPA and MySQL without touching controllers.
