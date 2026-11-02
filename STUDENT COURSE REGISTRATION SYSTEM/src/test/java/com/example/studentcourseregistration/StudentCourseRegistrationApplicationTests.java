package com.example.studentcourseregistration;

import com.example.studentcourseregistration.model.Student;
import com.example.studentcourseregistration.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Test Suite for Student Course Registration System.
 * Tests all 4 REST endpoints including all positive and negative test cases.
 */
@SpringBootTest
@AutoConfigureMockMvc
class StudentCourseRegistrationApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentService studentService;

    @Test
    @DisplayName("Application Context Loads Successfully")
    void contextLoads() {
        assertNotNull(studentService);
    }

    @Test
    @DisplayName("1. POST /api/students - Register Student (Success 201 Created)")
    void testRegisterStudentSuccess() throws Exception {
        String studentJson = """
                {
                    "studentName": "Varun",
                    "courseName": "Java Full Stack",
                    "email": "varun@gmail.com",
                    "age": 22
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId", notNullValue()))
                .andExpect(jsonPath("$.studentName", is("Varun")))
                .andExpect(jsonPath("$.courseName", is("Java Full Stack")))
                .andExpect(jsonPath("$.email", is("varun@gmail.com")))
                .andExpect(jsonPath("$.age", is(22)));
    }

    @Test
    @DisplayName("2. GET /api/students - View All Students (Success 200 OK)")
    void testGetAllStudents() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(Iterable.class)));
    }

    @Test
    @DisplayName("3. GET /api/students/{id} - Find Student by ID (Success 200 OK)")
    void testFindStudentByIdSuccess() throws Exception {
        // Register a student to retrieve
        Student student = new Student(null, "Priya", "Spring Boot", "priya@example.com", 24);
        Student saved = studentService.registerStudent(student);

        mockMvc.perform(get("/api/students/{id}", saved.getStudentId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId", is(saved.getStudentId().intValue())))
                .andExpect(jsonPath("$.studentName", is("Priya")))
                .andExpect(jsonPath("$.courseName", is("Spring Boot")));
    }

    @Test
    @DisplayName("4. GET /api/students/{id} - Non-Existing Student (Negative 404 Not Found)")
    void testFindStudentByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/students/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("Student not found with ID: 99999")));
    }

    @Test
    @DisplayName("5. DELETE /api/students/{id} - Delete Registration (Success 200 OK)")
    void testDeleteStudentSuccess() throws Exception {
        // Register a student to delete
        Student student = new Student(null, "Rahul", "Python Django", "rahul@example.com", 21);
        Student saved = studentService.registerStudent(student);

        mockMvc.perform(delete("/api/students/{id}", saved.getStudentId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUCCESS")))
                .andExpect(jsonPath("$.message", containsString("successfully deleted")));
    }

    @Test
    @DisplayName("6. DELETE /api/students/{id} - Delete Non-Existing Student (Negative 404 Not Found)")
    void testDeleteStudentNotFound() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 88888))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("Student not found with ID: 88888")));
    }

    @Test
    @DisplayName("7. POST /api/students - Validation: Empty Student Name (Negative 400 Bad Request)")
    void testValidationEmptyStudentName() throws Exception {
        String studentJson = """
                {
                    "studentName": "",
                    "courseName": "Java Full Stack",
                    "email": "test@gmail.com",
                    "age": 22
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors.studentName", is("Student name cannot be empty")));
    }

    @Test
    @DisplayName("8. POST /api/students - Validation: Invalid Email Format (Negative 400 Bad Request)")
    void testValidationInvalidEmail() throws Exception {
        String studentJson = """
                {
                    "studentName": "Varun",
                    "courseName": "Java Full Stack",
                    "email": "invalid-email-format",
                    "age": 22
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors.email", is("Enter valid email")));
    }

    @Test
    @DisplayName("9. POST /api/students - Validation: Empty Email (Negative 400 Bad Request)")
    void testValidationEmptyEmail() throws Exception {
        String studentJson = """
                {
                    "studentName": "Varun",
                    "courseName": "Java Full Stack",
                    "email": "",
                    "age": 22
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors.email", is("Email cannot be empty")));
    }

    @Test
    @DisplayName("10. POST /api/students - Validation: Age Below 18 (Negative 400 Bad Request)")
    void testValidationAgeBelow18() throws Exception {
        String studentJson = """
                {
                    "studentName": "Kiran",
                    "courseName": "React JS",
                    "email": "kiran@gmail.com",
                    "age": 16
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors.age", is("Age must be above 18")));
    }

    @Test
    @DisplayName("11. POST /api/students - Validation: Age Null (Negative 400 Bad Request)")
    void testValidationNullAge() throws Exception {
        String studentJson = """
                {
                    "studentName": "Kiran",
                    "courseName": "React JS",
                    "email": "kiran@gmail.com"
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors.age", is("Age cannot be null")));
    }

    @Test
    @DisplayName("12. POST /api/students - Validation: Empty Course Name (Negative 400 Bad Request)")
    void testValidationEmptyCourseName() throws Exception {
        String studentJson = """
                {
                    "studentName": "Varun",
                    "courseName": "",
                    "email": "varun@gmail.com",
                    "age": 22
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors.courseName", is("Course name cannot be empty")));
    }

    @Test
    @DisplayName("13. POST /api/students - Malformed JSON (Negative 400 Bad Request)")
    void testMalformedJson() throws Exception {
        String brokenJson = "{\"studentName\": \"Broken JSON without closing";

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brokenJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Malformed JSON request body")));
    }

    @Test
    @DisplayName("14. GET /api/students/{id} - Invalid ID Type Parameter (Negative 400 Bad Request)")
    void testInvalidIdType() throws Exception {
        mockMvc.perform(get("/api/students/{id}", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("Parameter 'id' must be of type Long")));
    }
}
