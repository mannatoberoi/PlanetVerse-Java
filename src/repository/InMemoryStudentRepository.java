package repository;

import exception.DuplicateStudentException;
import exception.StudentNotFoundException;
import model.Student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InMemoryStudentRepository
        implements Repository<Student, Integer> {

    private final List<Student> students = new ArrayList<>();

    private final Map<Integer, Student> studentsById =
            new HashMap<>();

    private final Set<String> studentEmails =
            new HashSet<>();

    // Add student
    @Override
    public synchronized boolean save(Student student) {

        if (student == null) {
            throw new IllegalArgumentException(
                "Student cannot be null"
            );
        }

        if (studentsById.containsKey(student.getUserId())) {
            throw new DuplicateStudentException(
                "Student ID already exists"
            );
        }

        String email = normalizeEmail(student.getEmail());

        if (studentEmails.contains(email)) {
            throw new DuplicateStudentException(
                "Student email already exists"
            );
        }

        students.add(student);
        studentsById.put(student.getUserId(), student);
        studentEmails.add(email);

        return true;
    }

    // Find student by ID
    @Override
    public synchronized Student findById(Integer id) {

        if (id == null) {
            return null;
        }

        return studentsById.get(id);
    }

    // Find student by email
    public synchronized Student findByEmail(String email) {

        if (email == null) {
            return null;
        }

        String normalizedEmail = normalizeEmail(email);

        for (Student student : students) {

            if (normalizeEmail(student.getEmail())
                    .equals(normalizedEmail)) {

                return student;
            }
        }

        return null;
    }

    // Get all students
    @Override
    public synchronized List<Student> findAll() {
        return new ArrayList<>(students);
    }

    // Delete student
    @Override
    public synchronized boolean delete(Integer id) {

        if (id == null) {
            return false;
        }

        Student student = studentsById.remove(id);

        if (student == null) {
            return false;
        }

        students.remove(student);
        studentEmails.remove(
            normalizeEmail(student.getEmail())
        );

        return true;
    }

    // Update student
    public synchronized boolean update(Student updatedStudent) {

        if (updatedStudent == null) {
            throw new IllegalArgumentException(
                "Student cannot be null"
            );
        }

        Student existing =
            studentsById.get(updatedStudent.getUserId());

        if (existing == null) {
            throw new StudentNotFoundException(
                "Student not found: "
                + updatedStudent.getUserId()
            );
        }

        String newEmail =
            normalizeEmail(updatedStudent.getEmail());

        Student emailOwner = findByEmail(newEmail);

        if (emailOwner != null &&
            emailOwner.getUserId() != updatedStudent.getUserId()) {

            throw new DuplicateStudentException(
                "Email already belongs to another student"
            );
        }

        studentEmails.remove(
            normalizeEmail(existing.getEmail())
        );

        studentEmails.add(newEmail);

        int index = students.indexOf(existing);

        students.set(index, updatedStudent);

        studentsById.put(
            updatedStudent.getUserId(),
            updatedStudent
        );

        return true;
    }

    // Sort students by CGPA, highest first
    public synchronized List<Student>
            findAllSortedByCgpaDescending() {

        List<Student> sortedStudents =
            new ArrayList<>(students);

        sortedStudents.sort(
            Comparator.comparing(Student::getCgpa)
                .reversed()
                .thenComparing(
                    Student::getName,
                    String.CASE_INSENSITIVE_ORDER
                )
        );

        return sortedStudents;
    }

    // Get total student count
    public synchronized int size() {
        return students.size();
    }

    // Normalize email
    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}