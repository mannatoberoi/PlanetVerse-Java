# PlanetVerse-Java — UML Class Diagram (Draft)

This diagram is based on the class names and relationships confirmed so far. Verify remaining method-level details against the source before using it as a definitive UML diagram.

```mermaid
classDiagram
    User <|-- Student
    User <|-- Instructor
    Eligible <|.. Course

    Student "1" --> "0..*" Enrollment : student
    Course "1" --> "0..*" Enrollment : course

    class User
    class Student {
        department
        cgpa
    }
    class Instructor
    class Eligible {
        <<interface>>
        isEligible(Student)
    }
    class Course {
        courseId
        courseName
        minimumCgpa
        capacity
        isEligible(Student)
    }
    class Enrollment {
        enrollmentId
        enrollmentDate
        status
    }
    class EnrollmentStatus {
        <<enumeration>>
        ENROLLED
        REJECTED
    }
```

## Core domain classes

- **User:** Shared superclass for user-related properties.
- **Student:** Student record; extends `User`.
- **Instructor:** Extends `User` (as reflected in the project's current model).
- **Eligible:** Eligibility-check contract.
- **Course:** Course information and eligibility logic.
- **Enrollment:** Represents a student's enrollment in a course.
- **EnrollmentStatus:** Enrollment state values.

## Layer-level relationships (overview)

```mermaid
flowchart TD
    App[PlanetVerseApp] --> Service[CourseService / StudentService]
    Service --> DAO[StudentDAO / CourseDAO / EnrollmentDAO]
    Service --> TDAO[TransactionalEnrollmentDAO]
    DAO --> DB[DBConnection]
    TDAO --> DB
    DB --> MySQL[(MySQL)]
    Service --> Models[Model classes]
    DAO --> Models
```

## Notes

- The domain diagram shows relationships that have been confirmed in prior code discussions.
- The layer-level diagram is an overview; exact dependencies should be checked against imports and method calls.
- This draft intentionally avoids inventing method signatures or composition relationships that have not been verified.
