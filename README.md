# PlanetVerse-Java

A Java-based student and course enrollment management application developed as a programming project. PlanetVerse-Java demonstrates object-oriented programming, collections, exception handling, concurrency, synchronization, producer-consumer workflows, the Executor framework, and JDBC-based database operations.

## Features

- Register and manage student records
- Add and view courses
- Enroll students in courses
- Check enrollment eligibility
- View all enrollments and enrollments for an individual student
- Prevent duplicate student-course enrollments
- Enforce course capacity during concurrent enrollment requests
- Persist data in MySQL using JDBC

## Technologies

- Java
- MySQL
- JDBC
- Java Collections Framework
- Threads, synchronization and semaphores
- Producer–Consumer pattern
- ExecutorService, Callable and Future
- Git and GitHub

## Project Structure

```text
PlanetVerse-Java/
├── src/
│   ├── concurrency/   # Concurrent enrollment and producer-consumer classes
│   ├── dao/           # Database access objects
│   ├── database/      # JDBC connection setup
│   ├── exception/     # Custom exceptions
│   ├── main/          # Application and test classes
│   ├── model/         # Student, Course, Enrollment and related models
│   ├── repository/    # Repository implementations
│   ├── service/       # Application/service logic
│   └── util/          # Utility classes
├── lib/               # MySQL JDBC connector
├── sql/               # Database scripts
├── data/              # Project data files, if applicable
├── docs/               # Project documentation and test evidence
└── README.md
```

## Database Setup

1. Install and start MySQL.
2. Create a database named:

   ```sql
   CREATE DATABASE planetverse_java;
   ```

3. Run the SQL setup scripts in the project's `sql/` directory to create the tables.
4. Set the database credentials as environment variables before running the application:

   **macOS/Linux (zsh):**

   ```bash
   export DB_USER="your_mysql_username"
   export DB_PASSWORD="your_mysql_password"
   ```

   Set these in your terminal session (or configure them securely in your environment). Do not commit credentials.

## JDBC Connector

The project uses the MySQL Connector/J jar located in `lib/`.

The connector filename currently used in the compile/run commands is:

```text
lib/mysql-connector-j-26.10.0.jar
```

## Compile

From the project root:

```bash
mkdir -p out
javac -cp "lib/mysql-connector-j-26.10.0.jar" -d out $(find src -name "*.java")
```

## Run the Application

```bash
java -cp "out:lib/mysql-connector-j-26.10.0.jar" main.PlanetVerseApp
```

## Application Menu

The console application provides options to:

1. Register Student
2. View Students
3. Add Course
4. View Courses
5. Enroll Student
6. View All Enrollments
7. View Student Enrollments
8. Exit

## Testing

Test classes are located in `src/main/`. The project has been tested for the following areas during development:

- Student collections and record-management operations
- Semaphore-based capacity control
- Database enrollment transactions and duplicate prevention
- Concurrent enrollment capacity handling
- Producer-consumer request processing
- Executor framework tasks using Callable and Future
- Console application's student, course and enrollment operations

See the `docs/` folder for test evidence and other project documentation.

## Java Concepts Demonstrated

- Classes, objects, inheritance and interfaces
- Encapsulation and model classes
- Collections such as ArrayList, HashMap and HashSet
- Custom exception handling
- JDBC and SQL operations
- Transactions and database row locking
- Multithreading and synchronization
- Semaphores
- Producer-consumer pattern
- ExecutorService, Callable and Future

## Team

Project developed by the PlanetVerse-Java team.

## Repository

GitHub: https://github.com/mannatoberoi/PlanetVerse-Java
