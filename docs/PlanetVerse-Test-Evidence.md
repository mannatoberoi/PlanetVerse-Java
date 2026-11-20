# PlanetVerse-Java — Test Evidence

> This document records tests performed during development. Test outcomes are based on the results confirmed during the project workflow.

## 1. Collections and Student Records

**Areas covered:** `ArrayList`, `HashMap`, `HashSet`, student lookup, CGPA sorting, duplicate email handling, and deletion.

- Tested adding and retrieving students from the in-memory collection repository.
- Tested lookup by student ID and email.
- Tested sorting students by CGPA.
- Tested duplicate email rejection.
- Tested student deletion.

**Result:** TestStudentCollections passed.

## 2. Semaphore and Concurrent Enrollment Capacity

**Areas covered:** concurrent access and semaphore-controlled slots.

- Ran multiple requesters against a course with limited capacity.
- Tested a capacity of two slots with five requesters.
- Verified that only two requesters successfully acquired slots.

**Result:** Semaphore capacity test passed; two successful slot acquisitions for a capacity of two.

## 3. JDBC Transactions and Enrollment Integrity

**Areas covered:** JDBC, transaction handling, row locking, eligibility, duplicate enrollment checks, and course capacity.

- Tested a successful enrollment transaction and insertion.
- Tested rejection of a duplicate enrollment.
- Tested concurrent enrollment requests against a course with capacity two.
- Tested five students/requesters against the limited-capacity course and confirmed exactly two enrollments succeeded.

**Result:** Transaction tests passed, including the limited-capacity concurrency test.

## 4. Producer–Consumer

**Areas covered:** producer-consumer queue, synchronization, request processing, and database-backed enrollment.

- Ran the producer-consumer test with five enrollment requests.
- Confirmed that the requests were processed and five enrollment records were reported in the test database.
- Test cleanup was performed afterward as reported during development.

**Result:** Producer-consumer test passed.

## 5. Executor Framework

**Areas covered:** `ExecutorService`, `Callable`, and `Future`.

- Submitted callable tasks through the executor framework.
- Retrieved task results through `Future`.
- Confirmed successful retrieval of three task results in the test.

**Result:** Executor framework test passed.

## 6. Console Application Functional Test

**Application:** `main.PlanetVerseApp`

The following menu workflows were tested:

1. Register a student.
2. View students.
3. Add a course.
4. View courses.
5. Enroll a student.
6. View all enrollments.
7. View enrollments for a student.
8. Exit.

**Result:** The user confirmed that all eight menu workflows were working.

## 7. Build and Repository Checks

- Compiled the Java source files using the MySQL Connector/J classpath.
- Confirmed the project tests were working after merging teammate changes.
- Added the console application and test files to Git and pushed the commit to `main`.

## Test Summary

| Test area | Result |
|---|---|
| Collections / student records | Passed |
| Semaphore capacity | Passed |
| JDBC transaction enrollment | Passed |
| Concurrent transactional capacity | Passed |
| Producer–Consumer | Passed |
| ExecutorService / Callable / Future | Passed |
| Console app menu workflows | Passed |
| Compile / Git integration | Confirmed during development |

## Notes

This is a development test record, not an automated test report. It documents test outcomes confirmed during development and should be accompanied by screenshots, console output, or instructor-requested evidence where available.
