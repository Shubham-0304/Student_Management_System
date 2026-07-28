# Student Management System

A console-based Student Management System built with **Java, JDBC, and MySQL**.
Supports full CRUD (Create, Read, Update, Delete) for student records through a
clean, layered, object-oriented design.

## Features

- Add, view, update, delete, and search student records
- Every query is a `PreparedStatement` (SQL-injection safe, reusable execution plan)
- Encapsulated `Student` model — private fields, public getters/setters
- Layered packages: `model`, `dao`, `util` — each class has one job
- Indexed `name` column + explicit column selection for faster reads
- DB credentials live in `db.properties`, not hardcoded in Java

## Tech Stack

Java 17 · JDBC · MySQL 8 · Maven

## Project Structure

```
student-management-system/
├── pom.xml
├── sql/student_db.sql          # creates the database + table
└── src/main/
    ├── java/com/sms/
    │   ├── Main.java               # console menu
    │   ├── model/Student.java      # entity (encapsulation)
    │   ├── dao/StudentDAO.java     # CRUD logic
    │   └── util/DBConnection.java  # connection handling
    └── resources/db.properties     # DB URL / user / password
```

## Setup

1. **Create the database**
   ```bash
   mysql -u root -p < sql/student_db.sql
   ```

2. **Set your credentials** in `src/main/resources/db.properties`

3. **Build**
   ```bash
   mvn clean package
   ```

4. **Run**
   ```bash
   java -jar target/student-management-system.jar
   ```

### Running without Maven

1. Download `mysql-connector-j` from the [MySQL site](https://dev.mysql.com/downloads/connector/j/)
2. Compile:
   ```bash
   javac -d out $(find src/main/java -name "*.java")
   cp src/main/resources/db.properties out/
   ```
3. Run (use `;` instead of `:` on Windows):
   ```bash
   java -cp "out:mysql-connector-j-9.7.0.jar" com.sms.Main
   ```

## Possible Enhancements

- Swing/JavaFX GUI instead of a console menu
- Connection pooling (HikariCP)
- Pagination for large result sets
- Unit tests with JUnit + Mockito

## License

MIT — see [LICENSE](LICENSE).
