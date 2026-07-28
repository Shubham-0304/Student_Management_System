-- Student Management System: database setup

CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE IF NOT EXISTS students (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    age     INT NOT NULL,
    course  VARCHAR(50)  NOT NULL,
    email   VARCHAR(100) UNIQUE,
    phone   VARCHAR(20),
    INDEX idx_name (name)   -- speeds up the name-search query in StudentDAO
);

-- Optional sample rows so the app has something to show right away
INSERT INTO students (name, age, course, email, phone) VALUES
('Aisha Khan',   20, 'Computer Science',        'aisha.khan@example.com',   '555-0101'),
('Liam Chen',    22, 'Information Technology',  'liam.chen@example.com',    '555-0102'),
('Maria Garcia', 21, 'Software Engineering',    'maria.garcia@example.com', '555-0103');
