package com.example.studentcourseregistration.service;

import com.example.studentcourseregistration.exception.StudentNotFoundException;
import com.example.studentcourseregistration.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * StudentService
 * Handles the business logic and in-memory data management for students.
 * Separates data processing logic from HTTP controller logic.
 */
@Service
public class StudentService {

    // Thread-safe in-memory list to store registered students (No database used)
    private final List<Student> studentList = new CopyOnWriteArrayList<>();

    // Atomic counter to automatically generate sequential, unique student IDs (1, 2, 3...)
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Registers a new student.
     * Automatically assigns a unique ID and saves to the in-memory list.
     *
     * @param student the student registration details from the request
     * @return the saved student with generated studentId
     */
    public Student registerStudent(Student student) {
        // Assign auto-generated unique ID
        Long generatedId = idGenerator.getAndIncrement();
        student.setStudentId(generatedId);

        // Add to in-memory collection
        studentList.add(student);
        return student;
    }

    /**
     * Retrieves all registered students.
     *
     * @return List of all students currently in memory
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentList);
    }

    /**
     * Finds a single student by their unique ID.
     *
     * @param studentId the ID of the student to search for
     * @return the matching Student
     * @throws StudentNotFoundException if no student matches the ID
     */
    public Student getStudentById(Long studentId) {
        return studentList.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + studentId));
    }

    /**
     * Deletes a student registration by their unique ID.
     *
     * @param studentId the ID of the student to delete
     * @return true if deleted successfully
     * @throws StudentNotFoundException if the student does not exist
     */
    public boolean deleteStudentById(Long studentId) {
        // First verify that the student exists (throws 404 if not found)
        Student existingStudent = getStudentById(studentId);

        // Remove the student from the list
        return studentList.remove(existingStudent);
    }
}
