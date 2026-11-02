package com.example.studentcourseregistration.controller;

import com.example.studentcourseregistration.model.Student;
import com.example.studentcourseregistration.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * StudentController
 * Exposes REST API endpoints for Student Course Registration.
 * Handles HTTP requests, invokes the service layer, and returns HTTP responses.
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    // Service layer dependency
    private final StudentService studentService;

    /**
     * Constructor-based Dependency Injection.
     * Spring automatically provides an instance of StudentService.
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 1. Register Student
     * POST /api/students
     * Accepts student details in JSON body, validates fields using @Valid,
     * generates a unique studentId, and saves the student.
     *
     * @param student validated student payload from request body
     * @return HTTP 201 Created with the saved student object
     */
    @PostMapping
    public ResponseEntity<Student> registerStudent(@Valid @RequestBody Student student) {
        Student registeredStudent = studentService.registerStudent(student);
        return new ResponseEntity<>(registeredStudent, HttpStatus.CREATED);
    }

    /**
     * 2. View All Registered Students
     * GET /api/students
     * Returns the full list of all students currently registered.
     *
     * @return HTTP 200 OK with the list of students
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * 3. Find Student by ID
     * GET /api/students/{id}
     * Looks up a single registered student by their unique ID.
     *
     * @param id the studentId from the path variable
     * @return HTTP 200 OK with student details if found, or HTTP 404 Not Found if missing
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    /**
     * 4. Delete Registration
     * DELETE /api/students/{id}
     * Deletes the registration for the student with the specified ID.
     *
     * @param id the studentId from the path variable
     * @return HTTP 200 OK with a confirmation message, or HTTP 404 Not Found if missing
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudentById(id);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", "Student registration with ID " + id + " has been successfully deleted.");
        response.put("status", "SUCCESS");

        return ResponseEntity.ok(response);
    }
}
