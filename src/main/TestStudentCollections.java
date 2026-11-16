package main;

import exception.DuplicateStudentException;
import model.Student;
import repository.InMemoryStudentRepository;

public class TestStudentCollections {

    public static void main(String[] args) {

        InMemoryStudentRepository repository =
            new InMemoryStudentRepository();

        // Create students
        Student student1 = new Student(
            9101,
            "Aarav Sharma",
            "aarav@example.com",
            "CSE",
            8.7
        );

        Student student2 = new Student(
            9102,
            "Diya Verma",
            "diya@example.com",
            "IT",
            9.2
        );

        Student student3 = new Student(
            9103,
            "Kabir Singh",
            "kabir@example.com",
            "CSE",
            7.9
        );

        // Add students
        repository.save(student1);
        repository.save(student2);
        repository.save(student3);

        System.out.println("Students added successfully!");

        // Display all students
        System.out.println("\nALL STUDENTS");

        for (Student student : repository.findAll()) {
            System.out.println(student.displayProfile());
            System.out.println("-------------------");
        }

        // Search by ID
        System.out.println("\nSEARCH BY ID");

        Student found = repository.findById(9102);

        if (found != null) {
            System.out.println(found.displayProfile());
        }

        // Search by email
        System.out.println("\nSEARCH BY EMAIL");

        Student emailResult =
            repository.findByEmail("DIYA@example.com");

        if (emailResult != null) {
            System.out.println(emailResult.getName());
        }

        // Sort by CGPA
        System.out.println("\nSORTED BY CGPA");

        for (Student student :
                repository.findAllSortedByCgpaDescending()) {

            System.out.println(
                student.getName() + " - " + student.getCgpa()
            );
        }

        // Test duplicate email
        System.out.println("\nTEST DUPLICATE EMAIL");

        try {

            Student duplicate = new Student(
                9104,
                "Duplicate User",
                "aarav@example.com",
                "ECE",
                8.0
            );

            repository.save(duplicate);

        } catch (DuplicateStudentException e) {

            System.out.println(
                "Duplicate prevented: " + e.getMessage()
            );
        }

        // Delete student
        System.out.println("\nDELETE STUDENT");

        System.out.println(
            "Deleted: " + repository.delete(9103)
        );

        System.out.println(
            "Remaining students: " + repository.size()
        );
    }
}