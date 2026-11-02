# Online Food Order System - Spring Boot REST API

A beginner-friendly RESTful Web Service built using **Java 25**, **Spring Boot**, and **Jakarta Validation**. This project manages online food orders temporarily in memory using a Java `ArrayList` without needing an external database.

---

## Table of Contents
1. [Project Introduction](#1-project-introduction)
2. [Project Objective](#2-project-objective)
3. [Technologies Used](#3-technologies-used)
4. [Project Architecture](#4-project-architecture)
5. [Folder Structure](#5-folder-structure)
6. [Model Explanation](#6-model-explanation)
7. [Controller Explanation](#7-controller-explanation)
8. [Service Explanation](#8-service-explanation)
9. [Validation Explanation](#9-validation-explanation)
10. [Exception Handling Explanation](#10-exception-handling-explanation)
11. [All Six API Details](#11-all-six-api-details)
12. [How to Run the Project in Eclipse](#12-how-to-run-the-project-in-eclipse)
13. [How to Test Using Postman](#13-how-to-test-using-postman)
14. [Example Requests and Responses](#14-example-requests-and-responses)
15. [Limitations of In-Memory Storage](#15-limitations-of-in-memory-storage)

---

## 1. Project Introduction

The **Online Food Order System** is a backend service designed to simulate how modern food ordering platforms (such as Swiggy or Zomato) handle incoming food orders. 

Clients (such as mobile applications, web frontends, or API testing tools like Postman) interact with the application through standardized **HTTP REST APIs**. The application accepts order details, validates incoming data, assigns a unique order ID automatically, updates orders, and deletes orders.

---

## 2. Project Objective

- Understand core **Spring Boot** fundamentals: inversion of control, dependency injection, and layered architecture.
- Learn how to build clean **RESTful APIs** using standard HTTP methods (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`).
- Understand data transfer and serialization between Java Objects and JSON format using Jackson.
- Implement server-side **Jakarta Bean Validation** to protect the API against bad inputs.
- Create centralized **Exception Handling** with `@RestControllerAdvice` to produce clean, readable error messages.
- Store and manage data using Java's built-in Collections (`ArrayList`) in memory without database complexity.

---

## 3. Technologies Used

| Technology / Tool | Version / Details | Purpose |
| :--- | :--- | :--- |
| **Java** | Version 24 / 25 LTS | Core programming language |
| **Spring Boot** | 4.0.x / 3.x Starter | Application framework with embedded Tomcat server |
| **Spring Web MVC** | Included with Spring Boot | Dispatches HTTP requests and builds REST endpoints |
| **Jakarta Validation** | Hibernate Validator | Field validation annotations (`@NotBlank`, `@Min`, `@NotNull`, `@Positive`) |
| **Maven** | 3.10+ (via `mvnw`) | Dependency management and build tool |
| **Eclipse IDE** | 2024 / STS | Development and debugging environment |
| **Postman** | Version 10+ | Manual and automated API testing |

---

## 4. Project Architecture

The project follows a standard **Layered Architecture** (Separation of Concerns):

```
Client (Postman / Browser)
         │
         ▼  HTTP Request (JSON)
┌────────────────────────────────────────┐
│   Controller Layer (OrderController)   │  <-- Receives HTTP requests, validates inputs
└──────────────────┬─────────────────────┘
                   │  Java Method Calls
                   ▼
┌────────────────────────────────────────┐
│     Service Layer (OrderService)       │  <-- Business logic, ID generation, validation
└──────────────────┬─────────────────────┘
                   │  List Operations
                   ▼
┌────────────────────────────────────────┐
│ In-Memory Data Storage (ArrayList)     │  <-- Temporary memory storage
└────────────────────────────────────────┘
                   │
                   ▼  (If Exception Occurs)
┌────────────────────────────────────────┐
│ Exception Layer (GlobalExceptionHandler│  <-- Converts exceptions into clean JSON errors
└────────────────────────────────────────┘
```

### Responsibility of Layers:
1. **Model Layer (`com.example.onlinefoodorder.model`)**: Defines the data blueprint (`Order`).
2. **Controller Layer (`com.example.onlinefoodorder.controller`)**: Exposes REST endpoints, reads incoming HTTP data, and returns HTTP responses with appropriate status codes.
3. **Service Layer (`com.example.onlinefoodorder.service`)**: Contains business rules, generates sequential IDs, updates fields, and performs in-memory storage operations.
4. **Exception Layer (`com.example.onlinefoodorder.exception`)**: Catches exceptions thrown by controllers or services and returns clean, uniform JSON responses without technical stack traces.

---

## 5. Folder Structure

```
online-food-order/
│
├── .mvn/                                 # Maven wrapper configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── onlinefoodorder/
│   │   │               ├── OnlineFoodOrderApplication.java   # Main Spring Boot Runner
│   │   │               ├── controller/
│   │   │               │   └── OrderController.java          # REST API endpoints
│   │   │               ├── service/
│   │   │               │   └── OrderService.java             # Business logic & ArrayList
│   │   │               ├── model/
│   │   │               │   └── Order.java                    # Order model & validation
│   │   │               └── exception/
│   │   │                   ├── OrderNotFoundException.java   # Custom 404 Exception
│   │   │                   ├── ErrorResponse.java            # Standard JSON error DTO
│   │   │                   └── GlobalExceptionHandler.java   # @RestControllerAdvice
│   │   └── resources/
│   │       └── application.properties                        # Port and log settings
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── onlinefoodorder/
│                       └── OnlineFoodOrderApplicationTests.java  # Unit & Integration Tests
│
├── PRESENTATION/                         # Presentation slides, viva Q&A, and scripts
│   ├── Online_Food_Order_Presentation.pptx
│   ├── Presentation_Slides_Content.md
│   ├── Presentation_Speaking_Script.md
│   ├── Viva_Questions_And_Answers.md
│   └── Project_Explanation_Guide.md
│
├── Online_Food_Order_API.postman_collection.json # Ready-to-import Postman Collection
├── pom.xml                               # Maven project configuration & dependencies
├── mvnw / mvnw.cmd                       # Maven wrapper executables
└── README.md                             # Comprehensive project documentation
```

---

## 6. Model Explanation

The `Order.java` class represents a customer's food order.

```java
public class Order {
    private Long orderId;
    private String customerName;
    private String foodItem;
    private Integer quantity;
    private Double price;
    // constructors, getters, and setters
}
```

### Fields and Types:
- `orderId` (`Long`): Auto-generated unique number identifying the order.
- `customerName` (`String`): The name of the customer placing the order.
- `foodItem` (`String`): The name of the dish being ordered (e.g., "Chicken Biryani").
- `quantity` (`Integer`): Number of portions ordered.
- `price` (`Double`): Total cost of the order in local currency.

### Constructors:
1. **Default Constructor (`public Order() {}`)**: Required by Spring Boot and Jackson to reconstruct Java objects when receiving JSON from HTTP requests.
2. **Parameterized Constructor (`public Order(Long orderId, String customerName, String foodItem, Integer quantity, Double price)`)**: Convenient for programmatically creating test and order objects.

---

## 7. Controller Explanation

The `OrderController.java` class handles incoming HTTP web requests:

- Annotated with `@RestController`: Marks this class as a Spring REST Controller where all handler methods automatically serialize returned objects directly into JSON format.
- Annotated with `@RequestMapping("/api/orders")`: Establishes the base URL path for all endpoints in this controller.
- Dependency Injection: Injects `OrderService` using constructor injection (`@Autowired`).

### Controller Methods:
- `@GetMapping`: Handles `GET /api/orders` to fetch all orders.
- `@GetMapping("/{id}")`: Handles `GET /api/orders/{id}` to fetch a specific order by ID.
- `@PostMapping`: Handles `POST /api/orders` to create an order, returning `201 Created`.
- `@PutMapping("/{id}")`: Handles `PUT /api/orders/{id}` to replace all editable fields of an order.
- `@DeleteMapping("/{id}")`: Handles `DELETE /api/orders/{id}` to remove an order, returning `200 OK` with a confirmation message.
- `@PatchMapping("/{id}")`: Handles `PATCH /api/orders/{id}` to update only specific fields without overwriting other details.

---

## 8. Service Explanation

The `OrderService.java` class encapsulates all business rules:

- Annotated with `@Service`: Marks the class as a Spring Service Component managed by the Spring IoC container.
- Storage: Stores orders inside a `List<Order> orderList = new ArrayList<>()`.
- ID Generation: Uses `AtomicLong idCounter = new AtomicLong(1)` to produce sequential IDs (`1, 2, 3...`) safely across requests.
- Thread Safety: Methods are marked with `synchronized` to ensure safe concurrent access when multiple HTTP requests arrive simultaneously.
- Clean Separation: The controller never directly touches the `orderList`; all additions, lookups, updates, and deletions happen through service methods.

---

## 9. Validation Explanation

Jakarta Bean Validation annotations ensure that only valid data enters the system:

| Field | Validation Annotation | Rule & Error Message |
| :--- | :--- | :--- |
| `customerName` | `@NotBlank(message = "Customer name is required")` | Value cannot be null, empty, or whitespace only. |
| `foodItem` | `@NotBlank(message = "Food item cannot be empty")` | Value cannot be null, empty, or whitespace only. |
| `quantity` | `@NotNull(message = "Quantity is mandatory")`<br>`@Min(value = 1, message = "Quantity must be at least 1")` | Must be an integer greater than or equal to 1. |
| `price` | `@NotNull(message = "Price is mandatory")`<br>`@Positive(message = "Price must be positive and greater than zero")` | Must be a decimal number strictly greater than 0. |

### How Validation is Enforced:
1. **On `POST` and `PUT`**: The `@Valid` annotation on `@RequestBody Order order` triggers automatic validation before the controller method executes.
2. **On `PATCH`**: Because partial updates omit untouched fields, the service creates a merged order and validates the final object using `jakarta.validation.Validator`. If any constraint fails, a `ConstraintViolationException` is thrown.

---

## 10. Exception Handling Explanation

Centralized exception handling is implemented in `GlobalExceptionHandler.java` using `@RestControllerAdvice`:

1. **`OrderNotFoundException`**: Triggered when a requested order ID does not exist. Returns HTTP `404 Not Found`.
2. **`MethodArgumentNotValidException`**: Triggered when `@Valid` fails on `POST` or `PUT`. Returns HTTP `400 Bad Request` with a detailed `validationErrors` map containing the exact field names and violation messages.
3. **`ConstraintViolationException`**: Triggered when validation fails on a `PATCH` request. Returns HTTP `400 Bad Request`.
4. **`HttpMessageNotReadableException`**: Triggered when request JSON is malformed, missing, or has invalid data types (e.g., passing a string for a numeric field). Returns HTTP `400 Bad Request`.
5. **`MethodArgumentTypeMismatchException`**: Triggered when a path parameter has the wrong type (e.g., `/api/orders/abc`). Returns HTTP `400 Bad Request`.
6. **`Exception`**: Catch-all fallback for any unexpected system errors. Returns HTTP `500 Internal Server Error`.

### Standard Error Response Format:
```json
{
  "timestamp": "2026-11-02T13:46:13.189",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed. Please correct the invalid fields.",
  "validationErrors": {
    "customerName": "Customer name is required"
  }
}
```

---

## 11. All Six API Details

### A. GET ALL ORDERS
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/orders`
- **Request Body**: None
- **Expected Status**: `200 OK`
- **Description**: Returns a JSON array of all orders currently stored in memory.

### B. GET ORDER BY ID
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/orders/{id}` (e.g., `/api/orders/1`)
- **Request Body**: None
- **Expected Status**: `200 OK` (if found) or `404 Not Found` (if ID does not exist)

### C. ADD NEW ORDER
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/orders`
- **Header**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "customerName": "Varun",
  "foodItem": "Chicken Biryani",
  "quantity": 2,
  "price": 250.0
}
```
- **Expected Status**: `201 Created`
- **Expected Response**: Created Order object with auto-assigned `orderId`.

### D. UPDATE ORDER (PUT)
- **Method**: `PUT`
- **URL**: `http://localhost:8080/api/orders/{id}` (e.g., `/api/orders/1`)
- **Header**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "customerName": "Varun Urrinkala",
  "foodItem": "Special Chicken Biryani",
  "quantity": 3,
  "price": 350.0
}
```
- **Expected Status**: `200 OK` (if found) or `404 Not Found` (if ID does not exist)

### E. DELETE ORDER
- **Method**: `DELETE`
- **URL**: `http://localhost:8080/api/orders/{id}` (e.g., `/api/orders/1`)
- **Request Body**: None
- **Expected Status**: `200 OK` (or `204 No Content`)
- **Expected Response**:
```json
{
  "message": "Order with ID 1 has been deleted successfully."
}
```

### F. PARTIAL UPDATE (PATCH)
- **Method**: `PATCH`
- **URL**: `http://localhost:8080/api/orders/{id}` (e.g., `/api/orders/1`)
- **Header**: `Content-Type: application/json`
- **Request Body**:
```json
{
  "quantity": 5
}
```
- **Expected Status**: `200 OK`
- **Description**: Updates only the `quantity` field to 5. Preserves customer name, food item, and price.

---

## 12. How to Run the Project in Eclipse

1. **Open Eclipse IDE**:
   - Launch Eclipse (Eclipse IDE for Java Developers or Spring Tools 4).
2. **Import the Project**:
   - Click `File` -> `Import...`
   - Select `Maven` -> `Existing Maven Projects` and click `Next`.
   - Click `Browse...` and select the project folder:
     `c:\Users\varun urrinkala\Downloads\online-food-order`
   - Ensure `pom.xml` is checked and click `Finish`.
3. **Wait for Maven Build**:
   - Eclipse will download all dependencies automatically.
4. **Run the Application**:
   - Navigate to `src/main/java/com/example/onlinefoodorder/OnlineFoodOrderApplication.java`.
   - Right-click on the file -> `Run As` -> `Java Application` (or `Spring Boot App`).
5. **Check Console**:
   - Look for the message: `Tomcat started on port 8080 (http) with context path '/'`.

---

## 13. How to Test Using Postman

1. **Open Postman**:
   - Launch the Postman application on your desktop.
2. **Import the Collection**:
   - Click the **Import** button in the top left corner of Postman.
   - Choose the file: `Online_Food_Order_API.postman_collection.json` located in the project folder.
   - Click **Import**.
3. **Execute Requests**:
   - The collection contains two subfolders:
     - `Positive Tests (Core 6 Operations)`
     - `Negative Tests (Validation & Exceptions)`
   - Run the requests sequentially to see the orders created, fetched, updated, patched, and deleted.

---

## 14. Example Requests and Responses

### Example 1: Add Order (POST)
**Request**: `POST http://localhost:8080/api/orders`
```json
{
  "customerName": "Varun",
  "foodItem": "Chicken Biryani",
  "quantity": 2,
  "price": 250.0
}
```
**Response**: `201 Created`
```json
{
  "orderId": 1,
  "customerName": "Varun",
  "foodItem": "Chicken Biryani",
  "quantity": 2,
  "price": 250.0
}
```

### Example 2: Negative Validation Test (POST with empty name & zero quantity)
**Request**: `POST http://localhost:8080/api/orders`
```json
{
  "customerName": "",
  "foodItem": "Pizza",
  "quantity": 0,
  "price": -50.0
}
```
**Response**: `400 Bad Request`
```json
{
  "timestamp": "2026-11-02T13:46:13.221",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed. Please correct the invalid fields.",
  "validationErrors": {
    "customerName": "Customer name is required",
    "quantity": "Quantity must be at least 1",
    "price": "Price must be positive and greater than zero"
  }
}
```

### Example 3: Get Non-Existent Order (GET)
**Request**: `GET http://localhost:8080/api/orders/999`
**Response**: `404 Not Found`
```json
{
  "timestamp": "2026-11-02T13:46:13.063",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found with ID: 999"
}
```

---

## 15. Limitations of In-Memory Storage

Storing data temporarily in an `ArrayList` provides simplicity for learning, but has fundamental limitations compared to real databases:

1. **Non-Persistent Data (Data Loss on Restart)**:
   - When the Spring Boot server shuts down or restarts, all data stored in memory is lost. Real-world systems use persistent databases (MySQL, PostgreSQL, Oracle).
2. **Memory Constraints (RAM Limited)**:
   - The number of orders that can be stored is constrained by the JVM heap memory. A database stores millions of records on disk.
3. **Lack of Advanced Querying & Indexing**:
   - Searching an `ArrayList` requires an $O(N)$ linear scan. Databases provide B-Trees, indexes, and SQL query optimization ($O(\log N)$ lookups).
4. **No ACID Transactions**:
   - An `ArrayList` does not support database transactions, rollback on failure, or write-ahead logging.
5. **Horizontal Scaling Issues**:
   - If multiple instances of the Spring Boot application are deployed behind a load balancer, each instance will have its own separate in-memory list, causing data inconsistency across servers.

---

## Verification & Build Summary

- Build status: **`BUILD SUCCESS`**
- All 6 unit/integration tests: **`PASS`**
- All 15 positive & negative API test cases: **`PASS`**
- Server running port: `8080`
